package lsieun.asm;

/**
 * A {@link ClassVisitor} that generates a corresponding ClassFile structure, as defined in the Java
 * Virtual Machine Specification (JVMS). It can be used alone, to generate a Java class "from
 * scratch", or with one or more {@link ClassReader} and adapter {@link ClassVisitor} to generate a
 * modified class from one or more existing Java classes.
 */
public class ClassWriter extends ClassVisitor {
    /**
     * A flag to automatically compute the maximum stack size and the maximum number of local
     * variables of methods. If this flag is set, then the arguments of the {@link
     * MethodVisitor#visitMaxs} method of the {@link MethodVisitor} returned by the {@link
     * #visitMethod} method will be ignored, and computed automatically from the signature and the
     * bytecode of each method.
     */
    public static final int COMPUTE_MAXS = 1;

    /**
     * A flag to automatically compute the stack map frames of methods from scratch. If this flag is
     * set, then the calls to the {@link MethodVisitor#visitFrame} method are ignored, and the stack
     * map frames are recomputed from the methods bytecode. The arguments of the {@link
     * MethodVisitor#visitMaxs} method are also ignored and recomputed from the bytecode. In other
     * words, {@link #COMPUTE_FRAMES} implies {@link #COMPUTE_MAXS}.
     */
    public static final int COMPUTE_FRAMES = 2;

    // region fields

    /**
     * Indicates what must be automatically computed in {@link MethodWriter}. Must be one of {@link
     * MethodWriter#COMPUTE_NOTHING}, {@link MethodWriter#COMPUTE_MAX_STACK_AND_LOCAL}, {@link
     * MethodWriter#COMPUTE_INSERTED_FRAMES}, or {@link MethodWriter#COMPUTE_ALL_FRAMES}.
     */
    private int compute;

    /**
     * The minor_version and major_version fields of the JVMS ClassFile structure. minor_version is
     * stored in the 16 most significant bits, and major_version in the 16 least significant bits.
     */
    private int version;

    /**
     * The symbol table for this class (contains the constant_pool).
     */
    private final SymbolTable symbolTable;

    /**
     * The access_flags field of the JVMS ClassFile structure.
     */
    private int accessFlags;

    /**
     * The this_class field of the JVMS ClassFile structure.
     */
    private int thisClass;

    /**
     * The super_class field of the JVMS ClassFile structure.
     */
    private int superClass;

    /**
     * The interface_count field of the JVMS ClassFile structure.
     */
    private int interfaceCount;

    /**
     * The 'interfaces' array of the JVMS ClassFile structure.
     */
    private int[] interfaces;

    /**
     * The fields of this class, stored in a linked list of {@link FieldWriter} linked via their
     * {@link FieldWriter#fv} field. This field stores the first element of this list.
     */
    private FieldWriter firstField;

    /**
     * The fields of this class, stored in a linked list of {@link FieldWriter} linked via their
     * {@link FieldWriter#fv} field. This field stores the last element of this list.
     */
    private FieldWriter lastField;

    /**
     * The methods of this class, stored in a linked list of {@link MethodWriter} linked via their
     * {@link MethodWriter#mv} field. This field stores the first element of this list.
     */
    private MethodWriter firstMethod;

    /**
     * The methods of this class, stored in a linked list of {@link MethodWriter} linked via their
     * {@link MethodWriter#mv} field. This field stores the last element of this list.
     */
    private MethodWriter lastMethod;

    /** The signature_index field of the Signature attribute, or 0. */
    private int signatureIndex;

    /**
     * The source_file_index field of the SourceFile attribute, or 0.
     */
    private int sourceFileIndex;

    /**
     * The first non standard attribute of this class. The next ones can be accessed with the {@link
     * Attribute#nextAttribute} field. May be {@literal null}.
     *
     * <p><b>WARNING</b>: this list stores the attributes in the <i>reverse</i> order of their visit.
     * firstAttribute is actually the last attribute visited in {@link #visitAttribute}. The {@link
     * #toByteArray} method writes the attributes in the order defined by this list, i.e. in the
     * reverse order specified by the user.
     */
    private Attribute firstAttribute;

    // endregion

    /**
     * Constructs a new {@link ClassVisitor}.
     *
     */
    public ClassWriter() {
        super(null);
        this.symbolTable = new SymbolTable(this);
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the ClassVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    // region @Override

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.accessFlags = access;
        this.thisClass = symbolTable.setMajorVersionAndClassName(version & 0xFFFF, name);
        if (signature != null) {
            this.signatureIndex = symbolTable.addConstantUtf8(signature);
        }
        this.superClass = superName == null ? 0 : symbolTable.addConstantClass(superName).index;
        if (interfaces != null && interfaces.length > 0) {
            interfaceCount = interfaces.length;
            this.interfaces = new int[interfaceCount];
            for (int i = 0; i < interfaceCount; ++i) {
                this.interfaces[i] = symbolTable.addConstantClass(interfaces[i]).index;
            }
        }
    }

    @Override
    public final void visitSource(final String file, final String debug) {
        if (file != null) {
            sourceFileIndex = symbolTable.addConstantUtf8(file);
        }
    }

    @Override
    public final FieldVisitor visitField(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        FieldWriter fieldWriter = new FieldWriter(symbolTable, access, name, descriptor, signature, value);
        if (firstField == null) {
            firstField = fieldWriter;
        } else {
            lastField.fv = fieldWriter;
        }
        return lastField = fieldWriter;
    }

    @Override
    public final MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        MethodWriter methodWriter = new MethodWriter(symbolTable, access, name, descriptor, signature, exceptions, compute);
        if (firstMethod == null) {
            firstMethod = methodWriter;
        } else {
            lastMethod.mv = methodWriter;
        }
        return lastMethod = methodWriter;
    }

    @Override
    public final void visitEnd() {
        // Nothing to do.
    }

    // endregion

    // -----------------------------------------------------------------------------------------------
    // Other public methods
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns the content of the class file that was built by this ClassWriter.
     *
     * @return the binary content of the JVMS ClassFile structure that was built by this ClassWriter.
     * @throws ClassTooLargeException  if the constant pool of the class is too large.
     * @throws MethodTooLargeException if the Code attribute of a method is too large.
     */
    public byte[] toByteArray() {
        // First step: compute the size in bytes of the ClassFile structure.
        // The magic field uses 4 bytes, 10 mandatory fields (minor_version, major_version,
        // constant_pool_count, access_flags, this_class, super_class, interfaces_count, fields_count,
        // methods_count and attributes_count) use 2 bytes each, and each interface uses 2 bytes too.
        int size = 24 + 2 * interfaceCount;
        int fieldsCount = 0;
        FieldWriter fieldWriter = firstField;
        while (fieldWriter != null) {
            ++fieldsCount;
            size += fieldWriter.computeFieldInfoSize();
            fieldWriter = (FieldWriter) fieldWriter.fv;
        }
        int methodsCount = 0;
        MethodWriter methodWriter = firstMethod;
        while (methodWriter != null) {
            ++methodsCount;
            size += methodWriter.computeMethodInfoSize();
            methodWriter = (MethodWriter) methodWriter.mv;
        }
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        int attributesCount = 0;
        if (sourceFileIndex != 0) {
            ++attributesCount;
            size += 8;
            symbolTable.addConstantUtf8(Constants.SOURCE_FILE);
        }
        if (firstAttribute != null) {
            attributesCount += firstAttribute.getAttributeCount();
            size += firstAttribute.computeAttributesSize(symbolTable);
        }
        // IMPORTANT: this must be the last part of the ClassFile size computation, because the previous
        // statements can add attribute names to the constant pool, thereby changing its size!
        size += symbolTable.getConstantPoolLength();
        int constantPoolCount = symbolTable.getConstantPoolCount();
        if (constantPoolCount > 0xFFFF) {
            throw new ClassTooLargeException(symbolTable.getClassName(), constantPoolCount);
        }

        // Second step: allocate a ByteVector of the correct size (in order to avoid any array copy in
        // dynamic resizes) and fill it with the ClassFile content.
        ByteVector result = new ByteVector(size);
        result.putInt(0xCAFEBABE).putInt(version);
        symbolTable.putConstantPool(result);
        int mask = 0;
        result.putShort(accessFlags & ~mask).putShort(thisClass).putShort(superClass);
        result.putShort(interfaceCount);
        for (int i = 0; i < interfaceCount; ++i) {
            result.putShort(interfaces[i]);
        }
        result.putShort(fieldsCount);
        fieldWriter = firstField;
        while (fieldWriter != null) {
            fieldWriter.putFieldInfo(result);
            fieldWriter = (FieldWriter) fieldWriter.fv;
        }
        result.putShort(methodsCount);
        boolean hasFrames = false;
        boolean hasAsmInstructions = false;
        methodWriter = firstMethod;
        while (methodWriter != null) {
            methodWriter.putMethodInfo(result);
            methodWriter = (MethodWriter) methodWriter.mv;
        }
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        result.putShort(attributesCount);
        if (sourceFileIndex != 0) {
            result
                    .putShort(symbolTable.addConstantUtf8(Constants.SOURCE_FILE))
                    .putInt(2)
                    .putShort(sourceFileIndex);
        }
        if (firstAttribute != null) {
            firstAttribute.putAttributes(symbolTable, result);
        }

        // Third step: replace the ASM specific instructions, if any.
        return result.data;
    }
}
