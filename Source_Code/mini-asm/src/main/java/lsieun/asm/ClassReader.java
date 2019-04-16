package lsieun.asm;

@SuppressWarnings("Duplicates")
public class ClassReader {


    /**
     * A byte array containing the JVMS ClassFile structure to be parsed. <i>The content of this array
     * must not be modified.</i>
     *
     */
    private final byte[] classFileBuffer;

    /**
     * The offset in bytes, in {@link #classFileBuffer}, of each cp_info entry of the ClassFile's
     * constant_pool array, <i>plus one</i>. In other words, the offset of constant pool entry i is
     * given by cpInfoOffsets[i] - 1, i.e. its cp_info's tag field is given by classFileBuffer[cpInfoOffsets[i] -
     * 1].
     */
    private final int[] cpInfoOffsets;

    /**
     * The String objects corresponding to the CONSTANT_Utf8 constant pool items. This cache avoids
     * multiple parsing of a given CONSTANT_Utf8 constant pool item.
     */
    private final String[] constantUtf8Values;

    /**
     * A conservative estimate of the maximum length of the strings contained in the constant pool of
     * the class.
     */
    private final int maxStringLength;
    private final char[] charBuffer;

    /** The offset in bytes of the ClassFile's access_flags field. */
    public final int header;

    public ClassReader(final byte[] classFileBuffer) {
        this.classFileBuffer = classFileBuffer;

        int constantPoolCount = readUnsignedShort(8);
        cpInfoOffsets = new int[constantPoolCount];
        constantUtf8Values = new String[constantPoolCount];

        // Compute the offset of each constant pool entry, as well as a conservative estimate of the
        // maximum length of the constant pool strings. The first constant pool entry is after the
        // magic, minor_version, major_version and constant_pool_count fields, which use 4, 2, 2 and 2
        // bytes respectively.
        int currentCpInfoIndex = 1; // 这里是“索引”
        int currentCpInfoOffset = 10; // 这里是“偏移量”
        int currentMaxStringLength = 0;

        // The offset of the other entries depend on the total size of all the previous entries.
        while (currentCpInfoIndex < constantPoolCount) {
            cpInfoOffsets[currentCpInfoIndex++] = currentCpInfoOffset + 1;
            int cpInfoSize;
            switch (classFileBuffer[currentCpInfoOffset]) {
                case Symbol.CONSTANT_INTEGER_TAG:
                case Symbol.CONSTANT_FLOAT_TAG:
                case Symbol.CONSTANT_FIELDREF_TAG:
                case Symbol.CONSTANT_METHODREF_TAG:
                case Symbol.CONSTANT_INTERFACE_METHODREF_TAG:
                case Symbol.CONSTANT_NAME_AND_TYPE_TAG:
                    cpInfoSize = 5;
                    break;
                case Symbol.CONSTANT_DYNAMIC_TAG:
                    cpInfoSize = 5;
                    break;
                case Symbol.CONSTANT_INVOKE_DYNAMIC_TAG:
                    cpInfoSize = 5;
                    break;
                case Symbol.CONSTANT_LONG_TAG:
                case Symbol.CONSTANT_DOUBLE_TAG:
                    cpInfoSize = 9;
                    currentCpInfoIndex++;
                    break;
                case Symbol.CONSTANT_UTF8_TAG:
                    cpInfoSize = 3 + readUnsignedShort(currentCpInfoOffset + 1);
                    if (cpInfoSize > currentMaxStringLength) {
                        // The size in bytes of this CONSTANT_Utf8 structure provides a conservative(保守的；守旧的) estimate
                        // of the length in characters of the corresponding string, and is much cheaper to
                        // compute than this exact length.
                        currentMaxStringLength = cpInfoSize;
                    }
                    break;
                case Symbol.CONSTANT_METHOD_HANDLE_TAG:
                    cpInfoSize = 4;
                    break;
                case Symbol.CONSTANT_CLASS_TAG:
                case Symbol.CONSTANT_STRING_TAG:
                case Symbol.CONSTANT_METHOD_TYPE_TAG:
                case Symbol.CONSTANT_PACKAGE_TAG:
                case Symbol.CONSTANT_MODULE_TAG:
                    cpInfoSize = 3;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            currentCpInfoOffset += cpInfoSize;
        }

        maxStringLength = currentMaxStringLength;
        charBuffer = new char[maxStringLength];
        // The Classfile's access_flags field is just after the last constant pool entry.
        header = currentCpInfoOffset;
    }

    // -----------------------------------------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns the class's access flags (see {@link Opcodes}). This value may not reflect Deprecated
     * and Synthetic flags when bytecode is before 1.5 and those flags are represented by attributes.
     *
     * @return the class access flags.
     * @see ClassVisitor#visit(int, int, String, String, String, String[])
     */
    public int getAccess() {
        return readUnsignedShort(header);
    }

    /**
     * Returns the internal name of the class (see {@link Type#getInternalName()}).
     *
     * @return the internal class name.
     * @see ClassVisitor#visit(int, int, String, String, String, String[])
     */
    public String getClassName() {
        // this_class is just after the access_flags field (using 2 bytes).
        int class_index = readUnsignedShort(header + 2);
        return readClassFromCP(class_index);
    }

    /**
     * Returns the internal of name of the super class (see {@link Type#getInternalName()}). For
     * interfaces, the super class is {@link Object}.
     *
     * @return the internal name of the super class, or {@literal null} for {@link Object} class.
     * @see ClassVisitor#visit(int, int, String, String, String, String[])
     */
    public String getSuperName() {
        // super_class is after the access_flags and this_class fields (2 bytes each).
        int class_index = readUnsignedShort(header + 4);
        return readClassFromCP(class_index);
    }

    /**
     * Returns the internal names of the implemented interfaces (see {@link Type#getInternalName()}).
     *
     * @return the internal names of the directly implemented interfaces. Inherited implemented
     *     interfaces are not returned.
     * @see ClassVisitor#visit(int, int, String, String, String, String[])
     */
    public String[] getInterfaces() {
        // interfaces_count is after the access_flags, this_class and super_class fields (2 bytes each).
        int currentOffset = header + 6;
        int interfacesCount = readUnsignedShort(currentOffset);
        String[] interfaces = new String[interfacesCount];
        if (interfacesCount > 0) {
            char[] charBuffer = new char[maxStringLength];
            for (int i = 0; i < interfacesCount; ++i) {
                currentOffset += 2;
                int class_index = readUnsignedShort(currentOffset);
                interfaces[i] = readClassFromCP(class_index);
            }
        }
        return interfaces;
    }

    // ----------------------------------------------------------------------------------------------
    // Methods to parse attributes
    // ----------------------------------------------------------------------------------------------

    /**
     * Returns the offset in {@link #classFileBuffer} of the first ClassFile's 'attributes' array
     * field entry.
     *
     * @return the offset in {@link #classFileBuffer} of the first ClassFile's 'attributes' array
     *     field entry.
     */
    final int getFirstAttributeOffset() {
        // Skip the access_flags, this_class, super_class, and interfaces_count fields (using 2 bytes
        // each), as well as the interfaces array field (2 bytes per interface).
        int currentOffset = header + 8 + readUnsignedShort(header + 6) * 2;

        // Read the fields_count field.
        int fieldsCount = readUnsignedShort(currentOffset);
        currentOffset += 2;
        // Skip the 'fields' array field.
        while (fieldsCount-- > 0) {
            // Invariant: currentOffset is the offset of a field_info structure.
            // Skip the access_flags, name_index and descriptor_index fields (2 bytes each), and read the
            // attributes_count field.
            int attributesCount = readUnsignedShort(currentOffset + 6);
            currentOffset += 8;
            // Skip the 'attributes' array field.
            while (attributesCount-- > 0) {
                // Invariant: currentOffset is the offset of an attribute_info structure.
                // Read the attribute_length field (2 bytes after the start of the attribute_info) and skip
                // this many bytes, plus 6 for the attribute_name_index and attribute_length fields
                // (yielding the total size of the attribute_info structure).
                currentOffset += 6 + readInt(currentOffset + 2);
            }
        }

        // Skip the methods_count and 'methods' fields, using the same method as above.
        int methodsCount = readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (methodsCount-- > 0) {
            int attributesCount = readUnsignedShort(currentOffset + 6);
            currentOffset += 8;
            while (attributesCount-- > 0) {
                currentOffset += 6 + readInt(currentOffset + 2);
            }
        }

        // Skip the ClassFile's attributes_count field.
        return currentOffset + 2;
    }

    /**
     * Makes the given visitor visit the JVMS ClassFile structure passed to the constructor of this
     * {@link ClassReader}.
     *
     * @param classVisitor the visitor that must visit this class.
     */
    public void accept(final ClassVisitor classVisitor){
        Context context = new Context();
        context.charBuffer = new char[maxStringLength];

        // Read the access_flags, this_class, super_class, interface_count and interfaces fields.
        int currentOffset = header;
        int this_class_index = readUnsignedShort(currentOffset + 2);
        int super_class_index = readUnsignedShort(currentOffset + 4);
        int interfaces_count = readUnsignedShort(currentOffset + 6);

        int accessFlags = readUnsignedShort(currentOffset);
        String thisClass = readClassFromCP(this_class_index);
        String superClass = readClassFromCP(super_class_index);
        String[] interfaces = new String[interfaces_count];
        currentOffset += 8;
        for (int i = 0; i < interfaces.length; ++i) {
            int class_index = readUnsignedShort(currentOffset);
            interfaces[i] = readClassFromCP(class_index);
            currentOffset += 2;
        }

        // - The string corresponding to the Signature attribute, or null.
        String signature = null;
        // - The string corresponding to the SourceFile attribute, or null.
        String sourceFile = null;

        int currentAttributeOffset = getFirstAttributeOffset();
        int attributes_count = readUnsignedShort(currentAttributeOffset - 2);
        for (int i = attributes_count; i > 0; --i) {
            // Read the attribute_info's attribute_name and attribute_length fields.
            int attribute_name_index = readUnsignedShort(currentAttributeOffset);
            String attributeName = readUTF8FromCP(attribute_name_index);
            int attributeLength = readInt(currentAttributeOffset + 2);
            currentAttributeOffset += 6;
            // The tests are sorted in decreasing frequency order (based on frequencies observed on
            // typical classes).
            if (Constants.SOURCE_FILE.equals(attributeName)) {
                int source_file_index = readUnsignedShort(currentAttributeOffset);
                sourceFile = readUTF8FromCP(source_file_index);
            } else if (Constants.SIGNATURE.equals(attributeName)) {
                int signature_index = readUnsignedShort(currentAttributeOffset);
                signature = readUTF8FromCP(signature_index);
            }
            currentAttributeOffset += attributeLength;
        }

        // Visit the class declaration. The minor_version and major_version fields start 6 bytes before
        // the first constant pool entry, which itself starts at cpInfoOffsets[1] - 1 (by definition).
        classVisitor.visit(readInt(cpInfoOffsets[1] - 7), accessFlags, thisClass, signature, superClass, interfaces);

        // Visit the fields and methods.
        int fieldsCount = readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (fieldsCount-- > 0) {
            currentOffset = readField(classVisitor, context, currentOffset);
        }

        int methodsCount = readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (methodsCount-- > 0) {
            currentOffset = readMethod(classVisitor, context, currentOffset);
        }

        // Visit the end of the class.
        classVisitor.visitEnd();
    }

    /**
     * Reads a JVMS field_info structure and makes the given visitor visit it.
     *
     * @param classVisitor the visitor that must visit the field.
     * @param context information about the class being parsed.
     * @param fieldInfoOffset the start offset of the field_info structure.
     * @return the offset of the first byte following the field_info structure.
     */

    private int readField(final ClassVisitor classVisitor, final Context context, final int fieldInfoOffset) {
        char[] charBuffer = context.charBuffer;

        // Read the access_flags, name_index and descriptor_index fields.
        int currentOffset = fieldInfoOffset;
        int name_index = readUnsignedShort(currentOffset + 2);
        int descriptor_index = readUnsignedShort(currentOffset + 4);

        int accessFlags = readUnsignedShort(currentOffset);
        String name = readUTF8FromCP(name_index);
        String descriptor = readUTF8FromCP(descriptor_index);
        currentOffset += 6;

        int attributesCount = readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            // Read the attribute_info's attribute_name and attribute_length fields.
            int attribute_name_index = readUnsignedShort(currentOffset);
            String attributeName = readUTF8FromCP(attribute_name_index);
            int attributeLength = readInt(currentOffset + 2);
            currentOffset += 6;
            currentOffset += attributeLength;
        }

        // Visit the field declaration.
        FieldVisitor fieldVisitor = classVisitor.visitField(accessFlags, name, descriptor, null, null);
        if (fieldVisitor == null) {
            return currentOffset;
        }

        // Visit the end of the field.
        fieldVisitor.visitEnd();
        return currentOffset;
    }

    /**
     * Reads a JVMS method_info structure and makes the given visitor visit it.
     *
     * @param classVisitor the visitor that must visit the method.
     * @param context information about the class being parsed.
     * @param methodInfoOffset the start offset of the method_info structure.
     * @return the offset of the first byte following the method_info structure.
     */
    private int readMethod(final ClassVisitor classVisitor, final Context context, final int methodInfoOffset) {
        char[] charBuffer = context.charBuffer;

        // Read the access_flags, name_index and descriptor_index fields.
        int currentOffset = methodInfoOffset;
        int method_name_index = readUnsignedShort(currentOffset + 2);
        int method_descripor_index = readUnsignedShort(currentOffset + 4);

        context.currentMethodAccessFlags = readUnsignedShort(currentOffset);
        context.currentMethodName = readUTF8FromCP(method_name_index);
        context.currentMethodDescriptor = readUTF8FromCP(method_descripor_index);
        currentOffset += 6;

        // Read the method attributes (the variables are ordered as in Section 4.7 of the JVMS).
        // Attribute offsets exclude the attribute_name_index and attribute_length fields.
        // - The offset of the Code attribute, or 0.
        int codeOffset = 0;

        int attributesCount = readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            // Read the attribute_info's attribute_name and attribute_length fields.
            int attribute_name_index = readUnsignedShort(currentOffset);
            String attributeName = readUTF8FromCP(attribute_name_index);
            int attributeLength = readInt(currentOffset + 2);
            currentOffset += 6;
            // The tests are sorted in decreasing frequency order (based on frequencies observed on
            // typical classes).
            if (Constants.CODE.equals(attributeName)) {
                codeOffset = currentOffset;
            }

            currentOffset += attributeLength;
        }

        // Visit the method declaration.
        MethodVisitor methodVisitor =
                classVisitor.visitMethod(
                        context.currentMethodAccessFlags,
                        context.currentMethodName,
                        context.currentMethodDescriptor,
                        null,
                        null);
        if (methodVisitor == null) {
            return currentOffset;
        }

        // Visit the end of the method.
        methodVisitor.visitEnd();
        return currentOffset;
    }

    // region read Bytes

    /**
     * Reads a byte value in this {@link ClassReader}. <i>This method is intended for {@link
     * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param offset the start offset of the value to be read in this {@link ClassReader}.
     * @return the read value.
     */
    public int readByte(final int offset) {
        return classFileBuffer[offset] & 0xFF;
    }

    /**
     * Reads an unsigned short value in this {@link ClassReader}. <i>This method is intended for
     * {@link Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param offset the start index of the value to be read in this {@link ClassReader}.
     * @return the read value.
     */
    public int readUnsignedShort(final int offset) {
        byte[] classBuffer = classFileBuffer;
        return ((classBuffer[offset] & 0xFF) << 8) | (classBuffer[offset + 1] & 0xFF);
    }

    /**
     * Reads a signed short value in this {@link ClassReader}. <i>This method is intended for {@link
     * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param offset the start offset of the value to be read in this {@link ClassReader}.
     * @return the read value.
     */
    public short readShort(final int offset) {
        byte[] classBuffer = classFileBuffer;
        return (short) (((classBuffer[offset] & 0xFF) << 8) | (classBuffer[offset + 1] & 0xFF));
    }

    /**
     * Reads a signed int value in this {@link ClassReader}. <i>This method is intended for {@link
     * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param offset the start offset of the value to be read in this {@link ClassReader}.
     * @return the read value.
     */
    public int readInt(final int offset) {
        byte[] classBuffer = classFileBuffer;
        return ((classBuffer[offset] & 0xFF) << 24)
                | ((classBuffer[offset + 1] & 0xFF) << 16)
                | ((classBuffer[offset + 2] & 0xFF) << 8)
                | (classBuffer[offset + 3] & 0xFF);
    }

    /**
     * Reads a signed long value in this {@link ClassReader}. <i>This method is intended for {@link
     * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param offset the start offset of the value to be read in this {@link ClassReader}.
     * @return the read value.
     */
    public long readLong(final int offset) {
        long l1 = readInt(offset);
        long l0 = readInt(offset + 4) & 0xFFFFFFFFL;
        return (l1 << 32) | l0;
    }

    /**
     * Reads an UTF8 string in {@link #classFileBuffer}.
     *
     * @param utfOffset the start offset of the UTF8 string to be read.
     * @param utfLength the length of the UTF8 string to be read.
     * @return the String corresponding to the specified UTF8 string.
     */
    private String readUTF8(final int utfOffset, final int utfLength) {
        int currentOffset = utfOffset;
        int endOffset = currentOffset + utfLength;
        int strLength = 0;
        byte[] classBuffer = classFileBuffer;
        while (currentOffset < endOffset) {
            int currentByte = classBuffer[currentOffset++];
            if ((currentByte & 0x80) == 0) {
                charBuffer[strLength++] = (char) (currentByte & 0x7F);
            } else if ((currentByte & 0xE0) == 0xC0) {
                charBuffer[strLength++] =
                        (char) (((currentByte & 0x1F) << 6) + (classBuffer[currentOffset++] & 0x3F));
            } else {
                charBuffer[strLength++] =
                        (char)
                                (((currentByte & 0xF) << 12)
                                        + ((classBuffer[currentOffset++] & 0x3F) << 6)
                                        + (classBuffer[currentOffset++] & 0x3F));
            }
        }
        return new String(charBuffer, 0, strLength);
    }
    // endregion

    // region read Constant Pool

    /**
     * Reads a CONSTANT_Utf8 constant pool entry in {@link #classFileBuffer}.
     *
     * @param constantPoolEntryIndex the index of a CONSTANT_Utf8 entry in the class's constant pool
     *     table.
     * @return the String corresponding to the specified CONSTANT_Utf8 entry.
     */
    final String readUTF8FromCP(final int constantPoolEntryIndex) {
        String value = constantUtf8Values[constantPoolEntryIndex];
        if (value != null) {
            return value;
        }
        int cpInfoOffset = cpInfoOffsets[constantPoolEntryIndex];
        value = readUTF8(cpInfoOffset + 2, readUnsignedShort(cpInfoOffset));
        return constantUtf8Values[constantPoolEntryIndex] = value;
    }

    /**
     * Reads a CONSTANT_Class constant pool entry in this {@link ClassReader}. <i>This method is
     * intended for {@link Attribute} sub classes, and is normally not needed by class generators or
     * adapters.</i>
     *
     * @param constantPoolEntryIndex the index of a CONSTANT_Class entry in class's constant pool table.
     * @return the String corresponding to the specified CONSTANT_Class entry.
     */
    public String readClassFromCP(final int constantPoolEntryIndex) {
        int classOffset = cpInfoOffsets[constantPoolEntryIndex]; // 此处对应CONSTANT_Class_info在classFileBuffer中的“偏移量”
        int utf8Index = readUnsignedShort(classOffset); //此处对应CONSTANT_Utf8_info在常量池中的“排序号”
        return readUTF8FromCP(utf8Index);
    }

    // endregion
}
