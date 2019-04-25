package lsieun.asm;

/**
 * A {@link MethodVisitor} that generates a corresponding 'method_info' structure, as defined in the
 * Java Virtual Machine Specification (JVMS).
 */
final class MethodWriter extends MethodVisitor {
    /** Indicates that nothing must be computed. */
    static final int COMPUTE_NOTHING = 0;

    /**
     * Indicates that the maximum stack size and the maximum number of local variables must be
     * computed, from scratch.
     */
    static final int COMPUTE_MAX_STACK_AND_LOCAL = 1;

    /**
     * Indicates that the maximum stack size and the maximum number of local variables must be
     * computed, from the existing stack map frames. This can be done more efficiently than with the
     * control flow graph algorithm used for {@link #COMPUTE_MAX_STACK_AND_LOCAL}, by using a linear
     * scan of the bytecode instructions.
     */
    static final int COMPUTE_MAX_STACK_AND_LOCAL_FROM_FRAMES = 2;

    /**
     * Indicates that the stack map frames of type F_INSERT must be computed. The other frames are not
     * computed. They should all be of type F_NEW and should be sufficient to compute the content of
     * the F_INSERT frames, together with the bytecode instructions between a F_NEW and a F_INSERT
     * frame - and without any knowledge of the type hierarchy (by definition of F_INSERT).
     */
    static final int COMPUTE_INSERTED_FRAMES = 3;

    /**
     * Indicates that all the stack map frames must be computed. In this case the maximum stack size
     * and the maximum number of local variables is also computed.
     */
    static final int COMPUTE_ALL_FRAMES = 4;

    /** Indicates that {@link #STACK_SIZE_DELTA} is not applicable (not constant or never used). */
    private static final int NA = 0;

    /**
     * The stack size variation corresponding to each JVM opcode. The stack size variation for opcode
     * 'o' is given by the array element at index 'o'.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-6.html">JVMS 6</a>
     */
    private static final int[] STACK_SIZE_DELTA = {
            // region values
            0, // nop = 0 (0x0)
            1, // aconst_null = 1 (0x1)
            1, // iconst_m1 = 2 (0x2)
            1, // iconst_0 = 3 (0x3)
            1, // iconst_1 = 4 (0x4)
            1, // iconst_2 = 5 (0x5)
            1, // iconst_3 = 6 (0x6)
            1, // iconst_4 = 7 (0x7)
            1, // iconst_5 = 8 (0x8)
            2, // lconst_0 = 9 (0x9)
            2, // lconst_1 = 10 (0xa)
            1, // fconst_0 = 11 (0xb)
            1, // fconst_1 = 12 (0xc)
            1, // fconst_2 = 13 (0xd)
            2, // dconst_0 = 14 (0xe)
            2, // dconst_1 = 15 (0xf)
            1, // bipush = 16 (0x10)
            1, // sipush = 17 (0x11)
            1, // ldc = 18 (0x12)
            NA, // ldc_w = 19 (0x13)
            NA, // ldc2_w = 20 (0x14)
            1, // iload = 21 (0x15)
            2, // lload = 22 (0x16)
            1, // fload = 23 (0x17)
            2, // dload = 24 (0x18)
            1, // aload = 25 (0x19)
            NA, // iload_0 = 26 (0x1a)
            NA, // iload_1 = 27 (0x1b)
            NA, // iload_2 = 28 (0x1c)
            NA, // iload_3 = 29 (0x1d)
            NA, // lload_0 = 30 (0x1e)
            NA, // lload_1 = 31 (0x1f)
            NA, // lload_2 = 32 (0x20)
            NA, // lload_3 = 33 (0x21)
            NA, // fload_0 = 34 (0x22)
            NA, // fload_1 = 35 (0x23)
            NA, // fload_2 = 36 (0x24)
            NA, // fload_3 = 37 (0x25)
            NA, // dload_0 = 38 (0x26)
            NA, // dload_1 = 39 (0x27)
            NA, // dload_2 = 40 (0x28)
            NA, // dload_3 = 41 (0x29)
            NA, // aload_0 = 42 (0x2a)
            NA, // aload_1 = 43 (0x2b)
            NA, // aload_2 = 44 (0x2c)
            NA, // aload_3 = 45 (0x2d)
            -1, // iaload = 46 (0x2e)
            0, // laload = 47 (0x2f)
            -1, // faload = 48 (0x30)
            0, // daload = 49 (0x31)
            -1, // aaload = 50 (0x32)
            -1, // baload = 51 (0x33)
            -1, // caload = 52 (0x34)
            -1, // saload = 53 (0x35)
            -1, // istore = 54 (0x36)
            -2, // lstore = 55 (0x37)
            -1, // fstore = 56 (0x38)
            -2, // dstore = 57 (0x39)
            -1, // astore = 58 (0x3a)
            NA, // istore_0 = 59 (0x3b)
            NA, // istore_1 = 60 (0x3c)
            NA, // istore_2 = 61 (0x3d)
            NA, // istore_3 = 62 (0x3e)
            NA, // lstore_0 = 63 (0x3f)
            NA, // lstore_1 = 64 (0x40)
            NA, // lstore_2 = 65 (0x41)
            NA, // lstore_3 = 66 (0x42)
            NA, // fstore_0 = 67 (0x43)
            NA, // fstore_1 = 68 (0x44)
            NA, // fstore_2 = 69 (0x45)
            NA, // fstore_3 = 70 (0x46)
            NA, // dstore_0 = 71 (0x47)
            NA, // dstore_1 = 72 (0x48)
            NA, // dstore_2 = 73 (0x49)
            NA, // dstore_3 = 74 (0x4a)
            NA, // astore_0 = 75 (0x4b)
            NA, // astore_1 = 76 (0x4c)
            NA, // astore_2 = 77 (0x4d)
            NA, // astore_3 = 78 (0x4e)
            -3, // iastore = 79 (0x4f)
            -4, // lastore = 80 (0x50)
            -3, // fastore = 81 (0x51)
            -4, // dastore = 82 (0x52)
            -3, // aastore = 83 (0x53)
            -3, // bastore = 84 (0x54)
            -3, // castore = 85 (0x55)
            -3, // sastore = 86 (0x56)
            -1, // pop = 87 (0x57)
            -2, // pop2 = 88 (0x58)
            1, // dup = 89 (0x59)
            1, // dup_x1 = 90 (0x5a)
            1, // dup_x2 = 91 (0x5b)
            2, // dup2 = 92 (0x5c)
            2, // dup2_x1 = 93 (0x5d)
            2, // dup2_x2 = 94 (0x5e)
            0, // swap = 95 (0x5f)
            -1, // iadd = 96 (0x60)
            -2, // ladd = 97 (0x61)
            -1, // fadd = 98 (0x62)
            -2, // dadd = 99 (0x63)
            -1, // isub = 100 (0x64)
            -2, // lsub = 101 (0x65)
            -1, // fsub = 102 (0x66)
            -2, // dsub = 103 (0x67)
            -1, // imul = 104 (0x68)
            -2, // lmul = 105 (0x69)
            -1, // fmul = 106 (0x6a)
            -2, // dmul = 107 (0x6b)
            -1, // idiv = 108 (0x6c)
            -2, // ldiv = 109 (0x6d)
            -1, // fdiv = 110 (0x6e)
            -2, // ddiv = 111 (0x6f)
            -1, // irem = 112 (0x70)
            -2, // lrem = 113 (0x71)
            -1, // frem = 114 (0x72)
            -2, // drem = 115 (0x73)
            0, // ineg = 116 (0x74)
            0, // lneg = 117 (0x75)
            0, // fneg = 118 (0x76)
            0, // dneg = 119 (0x77)
            -1, // ishl = 120 (0x78)
            -1, // lshl = 121 (0x79)
            -1, // ishr = 122 (0x7a)
            -1, // lshr = 123 (0x7b)
            -1, // iushr = 124 (0x7c)
            -1, // lushr = 125 (0x7d)
            -1, // iand = 126 (0x7e)
            -2, // land = 127 (0x7f)
            -1, // ior = 128 (0x80)
            -2, // lor = 129 (0x81)
            -1, // ixor = 130 (0x82)
            -2, // lxor = 131 (0x83)
            0, // iinc = 132 (0x84)
            1, // i2l = 133 (0x85)
            0, // i2f = 134 (0x86)
            1, // i2d = 135 (0x87)
            -1, // l2i = 136 (0x88)
            -1, // l2f = 137 (0x89)
            0, // l2d = 138 (0x8a)
            0, // f2i = 139 (0x8b)
            1, // f2l = 140 (0x8c)
            1, // f2d = 141 (0x8d)
            -1, // d2i = 142 (0x8e)
            0, // d2l = 143 (0x8f)
            -1, // d2f = 144 (0x90)
            0, // i2b = 145 (0x91)
            0, // i2c = 146 (0x92)
            0, // i2s = 147 (0x93)
            -3, // lcmp = 148 (0x94)
            -1, // fcmpl = 149 (0x95)
            -1, // fcmpg = 150 (0x96)
            -3, // dcmpl = 151 (0x97)
            -3, // dcmpg = 152 (0x98)
            -1, // ifeq = 153 (0x99)
            -1, // ifne = 154 (0x9a)
            -1, // iflt = 155 (0x9b)
            -1, // ifge = 156 (0x9c)
            -1, // ifgt = 157 (0x9d)
            -1, // ifle = 158 (0x9e)
            -2, // if_icmpeq = 159 (0x9f)
            -2, // if_icmpne = 160 (0xa0)
            -2, // if_icmplt = 161 (0xa1)
            -2, // if_icmpge = 162 (0xa2)
            -2, // if_icmpgt = 163 (0xa3)
            -2, // if_icmple = 164 (0xa4)
            -2, // if_acmpeq = 165 (0xa5)
            -2, // if_acmpne = 166 (0xa6)
            0, // goto = 167 (0xa7)
            1, // jsr = 168 (0xa8)
            0, // ret = 169 (0xa9)
            -1, // tableswitch = 170 (0xaa)
            -1, // lookupswitch = 171 (0xab)
            -1, // ireturn = 172 (0xac)
            -2, // lreturn = 173 (0xad)
            -1, // freturn = 174 (0xae)
            -2, // dreturn = 175 (0xaf)
            -1, // areturn = 176 (0xb0)
            0, // return = 177 (0xb1)
            NA, // getstatic = 178 (0xb2)
            NA, // putstatic = 179 (0xb3)
            NA, // getfield = 180 (0xb4)
            NA, // putfield = 181 (0xb5)
            NA, // invokevirtual = 182 (0xb6)
            NA, // invokespecial = 183 (0xb7)
            NA, // invokestatic = 184 (0xb8)
            NA, // invokeinterface = 185 (0xb9)
            NA, // invokedynamic = 186 (0xba)
            1, // new = 187 (0xbb)
            0, // newarray = 188 (0xbc)
            0, // anewarray = 189 (0xbd)
            0, // arraylength = 190 (0xbe)
            NA, // athrow = 191 (0xbf)
            0, // checkcast = 192 (0xc0)
            0, // instanceof = 193 (0xc1)
            -1, // monitorenter = 194 (0xc2)
            -1, // monitorexit = 195 (0xc3)
            NA, // wide = 196 (0xc4)
            NA, // multianewarray = 197 (0xc5)
            -1, // ifnull = 198 (0xc6)
            -1, // ifnonnull = 199 (0xc7)
            NA, // goto_w = 200 (0xc8)
            NA // jsr_w = 201 (0xc9)
            // endregion
    };

    /** Where the constants used in this MethodWriter must be stored. */
    private final SymbolTable symbolTable;

    /**
     * The access_flags field of the method_info JVMS structure. This field can contain ASM specific
     * access flags, such as {@link Opcodes#ACC_DEPRECATED}, which are removed when generating the
     * ClassFile structure.
     */
    private final int accessFlags;

    /** The name_index field of the method_info JVMS structure. */
    private final int nameIndex;

    /** The name of this method. */
    private final String name;

    /** The descriptor_index field of the method_info JVMS structure. */
    private final int descriptorIndex;

    /** The descriptor of this method. */
    private final String descriptor;

    // Code attribute fields and sub attributes:

    /** The max_stack field of the Code attribute. */
    private int maxStack;

    /** The max_locals field of the Code attribute. */
    private int maxLocals;

    /** The 'code' field of the Code attribute. */
    private final ByteVector code = new ByteVector();

    /**
     * The first non standard attribute of the Code attribute. The next ones can be accessed with the
     * {@link Attribute#nextAttribute} field. May be {@literal null}.
     *
     * <p><b>WARNING</b>: this list stores the attributes in the <i>reverse</i> order of their visit.
     * firstAttribute is actually the last attribute visited in {@link #visitAttribute}. The {@link
     * #putMethodInfo} method writes the attributes in the order defined by this list, i.e. in the
     * reverse order specified by the user.
     */
    private Attribute firstCodeAttribute;

    /**
     * The first non standard attribute of this method. The next ones can be accessed with the {@link
     * Attribute#nextAttribute} field. May be {@literal null}.
     *
     * <p><b>WARNING</b>: this list stores the attributes in the <i>reverse</i> order of their visit.
     * firstAttribute is actually the last attribute visited in {@link #visitAttribute}. The {@link
     * #putMethodInfo} method writes the attributes in the order defined by this list, i.e. in the
     * reverse order specified by the user.
     */
    private Attribute firstAttribute;

    /**
     * Indicates what must be computed. Must be one of {@link #COMPUTE_ALL_FRAMES}, {@link
     * #COMPUTE_INSERTED_FRAMES}, {@link #COMPUTE_MAX_STACK_AND_LOCAL} or {@link #COMPUTE_NOTHING}.
     */
    private final int compute;

    /** The parameters_count field of the MethodParameters attribute. */
    private int parametersCount;

    /** The 'parameters' array of the MethodParameters attribute, or {@literal null}. */
    private ByteVector parameters;

    /**
     * The start offset of the last visited instruction. Used to set the offset field of type
     * annotations of type 'offset_target' (see <a
     * href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.20.1">JVMS
     * 4.7.20.1</a>).
     */
    private int lastBytecodeOffset;

    /**
     * The offset in bytes in {@link SymbolTable#getSource} from which the method_info for this method
     * (excluding its first 6 bytes) must be copied, or 0.
     */
    private int sourceOffset;

    /**
     * The length in bytes in {@link SymbolTable#getSource} which must be copied to get the
     * method_info for this method (excluding its first 6 bytes for access_flags, name_index and
     * descriptor_index).
     */
    private int sourceLength;

    // -----------------------------------------------------------------------------------------------
    // Constructor and accessors
    // -----------------------------------------------------------------------------------------------

    /**
     * Constructs a new {@link MethodWriter}.
     *
     * @param symbolTable where the constants used in this AnnotationWriter must be stored.
     * @param access the method's access flags (see {@link Opcodes}).
     * @param name the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature the method's signature. May be {@literal null}.
     * @param exceptions the internal names of the method's exceptions. May be {@literal null}.
     * @param compute indicates what must be computed (see #compute).
     */
    MethodWriter(
            final SymbolTable symbolTable,
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions,
            final int compute) {
        super(null);
        this.symbolTable = symbolTable;
        this.accessFlags = "<init>".equals(name) ? access | Constants.ACC_CONSTRUCTOR : access;
        this.nameIndex = symbolTable.addConstantUtf8(name);
        this.name = name;
        this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
        this.descriptor = descriptor;
        this.compute = compute;
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the MethodVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    @Override
    public void visitParameter(final String name, final int access) {
        if (parameters == null) {
            parameters = new ByteVector();
        }
        ++parametersCount;
        parameters.putShort((name == null) ? 0 : symbolTable.addConstantUtf8(name)).putShort(access);
    }

    @Override
    public void visitAttribute(final Attribute attribute) {
        // Store the attributes in the <i>reverse</i> order of their visit by this method.
        if (attribute.isCodeAttribute()) {
            attribute.nextAttribute = firstCodeAttribute;
            firstCodeAttribute = attribute;
        } else {
            attribute.nextAttribute = firstAttribute;
            firstAttribute = attribute;
        }
    }

    @Override
    public void visitCode() {
        // Nothing to do.
    }

    @Override
    public void visitInsn(final int opcode) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        code.putByte(opcode);
        // If needed, update the maximum stack size and number of locals, and stack map frames.
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        if (opcode == Opcodes.SIPUSH) {
            code.put12(opcode, operand);
        } else { // BIPUSH or NEWARRAY
            code.put11(opcode, operand);
        }
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        if (var < 4 && opcode != Opcodes.RET) {
            int optimizedOpcode;
            if (opcode < Opcodes.ISTORE) {
                optimizedOpcode = Constants.ILOAD_0 + ((opcode - Opcodes.ILOAD) << 2) + var;
            } else {
                optimizedOpcode = Constants.ISTORE_0 + ((opcode - Opcodes.ISTORE) << 2) + var;
            }
            code.putByte(optimizedOpcode);
        } else if (var >= 256) {
            code.putByte(Constants.WIDE).put12(opcode, var);
        } else {
            code.put11(opcode, var);
        }
        // If needed, update the maximum stack size and number of locals, and stack map frames.
        if (compute != COMPUTE_NOTHING) {
            int currentMaxLocals;
            if (opcode == Opcodes.LLOAD
                    || opcode == Opcodes.DLOAD
                    || opcode == Opcodes.LSTORE
                    || opcode == Opcodes.DSTORE) {
                currentMaxLocals = var + 2;
            } else {
                currentMaxLocals = var + 1;
            }
            if (currentMaxLocals > maxLocals) {
                maxLocals = currentMaxLocals;
            }
        }
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        Symbol typeSymbol = symbolTable.addConstantClass(type);
        code.put12(opcode, typeSymbol.index);
        // If needed, update the maximum stack size and number of locals, and stack map frames.
    }

    @Override
    public void visitFieldInsn(
            final int opcode, final String owner, final String name, final String descriptor) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        Symbol fieldrefSymbol = symbolTable.addConstantFieldref(owner, name, descriptor);
        code.put12(opcode, fieldrefSymbol.index);
        // If needed, update the maximum stack size and number of locals, and stack map frames.
    }

    @Override
    public void visitMethodInsn(
            final int opcode,
            final String owner,
            final String name,
            final String descriptor,
            final boolean isInterface) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        Symbol methodrefSymbol = symbolTable.addConstantMethodref(owner, name, descriptor, isInterface);
        if (opcode == Opcodes.INVOKEINTERFACE) {
            code.put12(Opcodes.INVOKEINTERFACE, methodrefSymbol.index)
                    .put11(methodrefSymbol.getArgumentsAndReturnSizes() >> 2, 0);
        } else {
            code.put12(opcode, methodrefSymbol.index);
        }
        // If needed, update the maximum stack size and number of locals, and stack map frames.
    }

    @Override
    public void visitLdcInsn(final Object value) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        Symbol constantSymbol = symbolTable.addConstant(value);
        int constantIndex = constantSymbol.index;
        char firstDescriptorChar;
        boolean isLongOrDouble =
                constantSymbol.tag == Symbol.CONSTANT_LONG_TAG
                        || constantSymbol.tag == Symbol.CONSTANT_DOUBLE_TAG
                        || (constantSymbol.tag == Symbol.CONSTANT_DYNAMIC_TAG
                        && ((firstDescriptorChar = constantSymbol.value.charAt(0)) == 'J'
                        || firstDescriptorChar == 'D'));
        if (isLongOrDouble) {
            code.put12(Constants.LDC2_W, constantIndex);
        } else if (constantIndex >= 256) {
            code.put12(Constants.LDC_W, constantIndex);
        } else {
            code.put11(Opcodes.LDC, constantIndex);
        }
        // If needed, update the maximum stack size and number of locals, and stack map frames.
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        if ((var > 255) || (increment > 127) || (increment < -128)) {
            code.putByte(Constants.WIDE).put12(Opcodes.IINC, var).putShort(increment);
        } else {
            code.putByte(Opcodes.IINC).put11(var, increment);
        }
        // If needed, update the maximum stack size and number of locals, and stack map frames.
        if (compute != COMPUTE_NOTHING) {
            int currentMaxLocals = var + 1;
            if (currentMaxLocals > maxLocals) {
                maxLocals = currentMaxLocals;
            }
        }
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        if (compute == COMPUTE_ALL_FRAMES) {
            //computeAllFrames();
        } else if (compute == COMPUTE_MAX_STACK_AND_LOCAL) {
            //computeMaxStackAndLocal();
        } else if (compute == COMPUTE_MAX_STACK_AND_LOCAL_FROM_FRAMES) {
            //this.maxStack = maxRelativeStackSize;
        } else {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    /**
     * Returns the size of the method_info JVMS structure generated by this MethodWriter. Also add the
     * names of the attributes of this method in the constant pool.
     *
     * @return the size in bytes of the method_info JVMS structure.
     */
    int computeMethodInfoSize() {
        // If this method_info must be copied from an existing one, the size computation is trivial.
        if (sourceOffset != 0) {
            // sourceLength excludes the first 6 bytes for access_flags, name_index and descriptor_index.
            return 6 + sourceLength;
        }
        // 2 bytes each for access_flags, name_index, descriptor_index and attributes_count.
        int size = 8;
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        if (code.length > 0) {
            if (code.length > 65535) {
                throw new MethodTooLargeException(
                        symbolTable.getClassName(), name, descriptor, code.length);
            }
            symbolTable.addConstantUtf8(Constants.CODE);
            // The Code attribute has 6 header bytes, plus 2, 2, 4 and 2 bytes respectively for max_stack,
            // max_locals, code_length and attributes_count, plus the bytecode and the exception table.
            size += 16 + code.length;
            if (firstCodeAttribute != null) {
                size +=
                        firstCodeAttribute.computeAttributesSize(
                                symbolTable, code.data, code.length, maxStack, maxLocals);
            }
        }
        size += Attribute.computeAttributesSize(symbolTable, accessFlags, 0);
        if (parameters != null) {
            symbolTable.addConstantUtf8(Constants.METHOD_PARAMETERS);
            // 6 header bytes and 1 byte for parameters_count.
            size += 7 + parameters.length;
        }
        if (firstAttribute != null) {
            size += firstAttribute.computeAttributesSize(symbolTable);
        }
        return size;
    }

    /**
     * Puts the content of the method_info JVMS structure generated by this MethodWriter into the
     * given ByteVector.
     *
     * @param output where the method_info structure must be put.
     */
    void putMethodInfo(final ByteVector output) {
        int mask = 0;
        output.putShort(accessFlags & ~mask).putShort(nameIndex).putShort(descriptorIndex);
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        int attributeCount = 0;
        if (code.length > 0) {
            ++attributeCount;
        }
        if (parameters != null) {
            ++attributeCount;
        }
        if (firstAttribute != null) {
            attributeCount += firstAttribute.getAttributeCount();
        }
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        output.putShort(attributeCount);
        if (code.length > 0) {
            // 2, 2, 4 and 2 bytes respectively for max_stack, max_locals, code_length and
            // attributes_count, plus the bytecode and the exception table.
            int size = 10 + code.length;
            int codeAttributeCount = 0;
            if (firstCodeAttribute != null) {
                size +=
                        firstCodeAttribute.computeAttributesSize(
                                symbolTable, code.data, code.length, maxStack, maxLocals);
                codeAttributeCount += firstCodeAttribute.getAttributeCount();
            }
            output
                    .putShort(symbolTable.addConstantUtf8(Constants.CODE))
                    .putInt(size)
                    .putShort(maxStack)
                    .putShort(maxLocals)
                    .putInt(code.length)
                    .putByteArray(code.data, 0, code.length);
            output.putShort(codeAttributeCount);
            if (firstCodeAttribute != null) {
                firstCodeAttribute.putAttributes(
                        symbolTable, code.data, code.length, maxStack, maxLocals, output);
            }
        }
        Attribute.putAttributes(symbolTable, accessFlags, 0, output);
        if (parameters != null) {
            output
                    .putShort(symbolTable.addConstantUtf8(Constants.METHOD_PARAMETERS))
                    .putInt(1 + parameters.length)
                    .putByte(parametersCount)
                    .putByteArray(parameters.data, 0, parameters.length);
        }
        if (firstAttribute != null) {
            firstAttribute.putAttributes(symbolTable, output);
        }
    }
}
