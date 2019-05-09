package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.generic.cnst.OpcodeConst;

/**
 * This interface contains shareable instruction objects.
 * 
 * <p>
 * In order to save memory you can use some instructions multiply,
 * since they have an immutable state and are directly derived from
 * Instruction.  I.e. they have no instance fields that could be
 * changed. Since some of these instructions like ICONST_0 occur
 * very frequently this can save a lot of time and space.
 * </p><br/>
 * 
 * 使用FlyWeight设计模式<br/><br/>
 * <p>
 * This feature is an adaptation of the FlyWeight design pattern, we
 * just use an array instead of a factory.
 * </p><br/>
 *
 * <p>
 * The Instructions can also accessed directly under their names, so
 * it's possible to write il.append(Instruction.ICONST_0);
 * </p>
 *
 */
public class InstructionConst {
    /**
     * Predefined instruction objects
     */
    /*
     * NOTE these are not currently immutable, because Instruction
     * has mutable protected fields opcode and length.
     */
    public static final Instruction NOP = new NOP();
    public static final Instruction ACONST_NULL = new ACONST_NULL();
    public static final Instruction ICONST_M1 = new ICONST(-1);
    public static final Instruction ICONST_0 = new ICONST(0);
    public static final Instruction ICONST_1 = new ICONST(1);
    public static final Instruction ICONST_2 = new ICONST(2);
    public static final Instruction ICONST_3 = new ICONST(3);
    public static final Instruction ICONST_4 = new ICONST(4);
    public static final Instruction ICONST_5 = new ICONST(5);
    public static final Instruction LCONST_0 = new LCONST(0);
    public static final Instruction LCONST_1 = new LCONST(1);
    public static final Instruction FCONST_0 = new FCONST(0);
    public static final Instruction FCONST_1 = new FCONST(1);
    public static final Instruction FCONST_2 = new FCONST(2);
    public static final Instruction DCONST_0 = new DCONST(0);
    public static final Instruction DCONST_1 = new DCONST(1);
    public static final ArrayInstruction IALOAD = new IALOAD();
    public static final ArrayInstruction LALOAD = new LALOAD();
    public static final ArrayInstruction FALOAD = new FALOAD();
    public static final ArrayInstruction DALOAD = new DALOAD();
    public static final ArrayInstruction AALOAD = new AALOAD();
    public static final ArrayInstruction BALOAD = new BALOAD();
    public static final ArrayInstruction CALOAD = new CALOAD();
    public static final ArrayInstruction SALOAD = new SALOAD();
    public static final ArrayInstruction IASTORE = new IASTORE();
    public static final ArrayInstruction LASTORE = new LASTORE();
    public static final ArrayInstruction FASTORE = new FASTORE();
    public static final ArrayInstruction DASTORE = new DASTORE();
    public static final ArrayInstruction AASTORE = new AASTORE();
    public static final ArrayInstruction BASTORE = new BASTORE();
    public static final ArrayInstruction CASTORE = new CASTORE();
    public static final ArrayInstruction SASTORE = new SASTORE();
    public static final StackInstruction POP = new POP();
    public static final StackInstruction POP2 = new POP2();
    public static final StackInstruction DUP = new DUP();
    public static final StackInstruction DUP_X1 = new DUP_X1();
    public static final StackInstruction DUP_X2 = new DUP_X2();
    public static final StackInstruction DUP2 = new DUP2();
    public static final StackInstruction DUP2_X1 = new DUP2_X1();
    public static final StackInstruction DUP2_X2 = new DUP2_X2();
    public static final StackInstruction SWAP = new SWAP();
    public static final ArithmeticInstruction IADD = new IADD();
    public static final ArithmeticInstruction LADD = new LADD();
    public static final ArithmeticInstruction FADD = new FADD();
    public static final ArithmeticInstruction DADD = new DADD();
    public static final ArithmeticInstruction ISUB = new ISUB();
    public static final ArithmeticInstruction LSUB = new LSUB();
    public static final ArithmeticInstruction FSUB = new FSUB();
    public static final ArithmeticInstruction DSUB = new DSUB();
    public static final ArithmeticInstruction IMUL = new IMUL();
    public static final ArithmeticInstruction LMUL = new LMUL();
    public static final ArithmeticInstruction FMUL = new FMUL();
    public static final ArithmeticInstruction DMUL = new DMUL();
    public static final ArithmeticInstruction IDIV = new IDIV();
    public static final ArithmeticInstruction LDIV = new LDIV();
    public static final ArithmeticInstruction FDIV = new FDIV();
    public static final ArithmeticInstruction DDIV = new DDIV();
    public static final ArithmeticInstruction IREM = new IREM();
    public static final ArithmeticInstruction LREM = new LREM();
    public static final ArithmeticInstruction FREM = new FREM();
    public static final ArithmeticInstruction DREM = new DREM();
    public static final ArithmeticInstruction INEG = new INEG();
    public static final ArithmeticInstruction LNEG = new LNEG();
    public static final ArithmeticInstruction FNEG = new FNEG();
    public static final ArithmeticInstruction DNEG = new DNEG();
    public static final ArithmeticInstruction ISHL = new ISHL();
    public static final ArithmeticInstruction LSHL = new LSHL();
    public static final ArithmeticInstruction ISHR = new ISHR();
    public static final ArithmeticInstruction LSHR = new LSHR();
    public static final ArithmeticInstruction IUSHR = new IUSHR();
    public static final ArithmeticInstruction LUSHR = new LUSHR();
    public static final ArithmeticInstruction IAND = new IAND();
    public static final ArithmeticInstruction LAND = new LAND();
    public static final ArithmeticInstruction IOR = new IOR();
    public static final ArithmeticInstruction LOR = new LOR();
    public static final ArithmeticInstruction IXOR = new IXOR();
    public static final ArithmeticInstruction LXOR = new LXOR();
    public static final ConversionInstruction I2L = new I2L();
    public static final ConversionInstruction I2F = new I2F();
    public static final ConversionInstruction I2D = new I2D();
    public static final ConversionInstruction L2I = new L2I();
    public static final ConversionInstruction L2F = new L2F();
    public static final ConversionInstruction L2D = new L2D();
    public static final ConversionInstruction F2I = new F2I();
    public static final ConversionInstruction F2L = new F2L();
    public static final ConversionInstruction F2D = new F2D();
    public static final ConversionInstruction D2I = new D2I();
    public static final ConversionInstruction D2L = new D2L();
    public static final ConversionInstruction D2F = new D2F();
    public static final ConversionInstruction I2B = new I2B();
    public static final ConversionInstruction I2C = new I2C();
    public static final ConversionInstruction I2S = new I2S();
    public static final Instruction LCMP = new LCMP();
    public static final Instruction FCMPL = new FCMPL();
    public static final Instruction FCMPG = new FCMPG();
    public static final Instruction DCMPL = new DCMPL();
    public static final Instruction DCMPG = new DCMPG();
    public static final ReturnInstruction IRETURN = new IRETURN();
    public static final ReturnInstruction LRETURN = new LRETURN();
    public static final ReturnInstruction FRETURN = new FRETURN();
    public static final ReturnInstruction DRETURN = new DRETURN();
    public static final ReturnInstruction ARETURN = new ARETURN();
    public static final ReturnInstruction RETURN = new RETURN();
    public static final Instruction ARRAYLENGTH = new ARRAYLENGTH();
    public static final Instruction ATHROW = new ATHROW();
    public static final Instruction MONITORENTER = new MONITORENTER();
    public static final Instruction MONITOREXIT = new MONITOREXIT();

    /** You can use these constants in multiple places safely, if you can guarantee
     * that you will never alter their internal values, e.g. call setIndex().
     */
    public static final LocalVariableInstruction THIS = new ALOAD(0);
    public static final LocalVariableInstruction ALOAD_0 = THIS;
    public static final LocalVariableInstruction ALOAD_1 = new ALOAD(1);
    public static final LocalVariableInstruction ALOAD_2 = new ALOAD(2);
    public static final LocalVariableInstruction ILOAD_0 = new ILOAD(0);
    public static final LocalVariableInstruction ILOAD_1 = new ILOAD(1);
    public static final LocalVariableInstruction ILOAD_2 = new ILOAD(2);
    public static final LocalVariableInstruction ASTORE_0 = new ASTORE(0);
    public static final LocalVariableInstruction ASTORE_1 = new ASTORE(1);
    public static final LocalVariableInstruction ASTORE_2 = new ASTORE(2);
    public static final LocalVariableInstruction ISTORE_0 = new ISTORE(0);
    public static final LocalVariableInstruction ISTORE_1 = new ISTORE(1);
    public static final LocalVariableInstruction ISTORE_2 = new ISTORE(2);

    /** Get object via its opcode, for immutable instructions like
     * branch instructions entries are set to null.
     */
    private static final Instruction[] INSTRUCTIONS = new Instruction[256];

    static {
        INSTRUCTIONS[OpcodeConst.NOP] = NOP;
        INSTRUCTIONS[OpcodeConst.ACONST_NULL] = ACONST_NULL;
        INSTRUCTIONS[OpcodeConst.ICONST_M1] = ICONST_M1;
        INSTRUCTIONS[OpcodeConst.ICONST_0] = ICONST_0;
        INSTRUCTIONS[OpcodeConst.ICONST_1] = ICONST_1;
        INSTRUCTIONS[OpcodeConst.ICONST_2] = ICONST_2;
        INSTRUCTIONS[OpcodeConst.ICONST_3] = ICONST_3;
        INSTRUCTIONS[OpcodeConst.ICONST_4] = ICONST_4;
        INSTRUCTIONS[OpcodeConst.ICONST_5] = ICONST_5;
        INSTRUCTIONS[OpcodeConst.LCONST_0] = LCONST_0;
        INSTRUCTIONS[OpcodeConst.LCONST_1] = LCONST_1;
        INSTRUCTIONS[OpcodeConst.FCONST_0] = FCONST_0;
        INSTRUCTIONS[OpcodeConst.FCONST_1] = FCONST_1;
        INSTRUCTIONS[OpcodeConst.FCONST_2] = FCONST_2;
        INSTRUCTIONS[OpcodeConst.DCONST_0] = DCONST_0;
        INSTRUCTIONS[OpcodeConst.DCONST_1] = DCONST_1;
        INSTRUCTIONS[OpcodeConst.IALOAD] = IALOAD;
        INSTRUCTIONS[OpcodeConst.LALOAD] = LALOAD;
        INSTRUCTIONS[OpcodeConst.FALOAD] = FALOAD;
        INSTRUCTIONS[OpcodeConst.DALOAD] = DALOAD;
        INSTRUCTIONS[OpcodeConst.AALOAD] = AALOAD;
        INSTRUCTIONS[OpcodeConst.BALOAD] = BALOAD;
        INSTRUCTIONS[OpcodeConst.CALOAD] = CALOAD;
        INSTRUCTIONS[OpcodeConst.SALOAD] = SALOAD;
        INSTRUCTIONS[OpcodeConst.IASTORE] = IASTORE;
        INSTRUCTIONS[OpcodeConst.LASTORE] = LASTORE;
        INSTRUCTIONS[OpcodeConst.FASTORE] = FASTORE;
        INSTRUCTIONS[OpcodeConst.DASTORE] = DASTORE;
        INSTRUCTIONS[OpcodeConst.AASTORE] = AASTORE;
        INSTRUCTIONS[OpcodeConst.BASTORE] = BASTORE;
        INSTRUCTIONS[OpcodeConst.CASTORE] = CASTORE;
        INSTRUCTIONS[OpcodeConst.SASTORE] = SASTORE;
        INSTRUCTIONS[OpcodeConst.POP] = POP;
        INSTRUCTIONS[OpcodeConst.POP2] = POP2;
        INSTRUCTIONS[OpcodeConst.DUP] = DUP;
        INSTRUCTIONS[OpcodeConst.DUP_X1] = DUP_X1;
        INSTRUCTIONS[OpcodeConst.DUP_X2] = DUP_X2;
        INSTRUCTIONS[OpcodeConst.DUP2] = DUP2;
        INSTRUCTIONS[OpcodeConst.DUP2_X1] = DUP2_X1;
        INSTRUCTIONS[OpcodeConst.DUP2_X2] = DUP2_X2;
        INSTRUCTIONS[OpcodeConst.SWAP] = SWAP;
        INSTRUCTIONS[OpcodeConst.IADD] = IADD;
        INSTRUCTIONS[OpcodeConst.LADD] = LADD;
        INSTRUCTIONS[OpcodeConst.FADD] = FADD;
        INSTRUCTIONS[OpcodeConst.DADD] = DADD;
        INSTRUCTIONS[OpcodeConst.ISUB] = ISUB;
        INSTRUCTIONS[OpcodeConst.LSUB] = LSUB;
        INSTRUCTIONS[OpcodeConst.FSUB] = FSUB;
        INSTRUCTIONS[OpcodeConst.DSUB] = DSUB;
        INSTRUCTIONS[OpcodeConst.IMUL] = IMUL;
        INSTRUCTIONS[OpcodeConst.LMUL] = LMUL;
        INSTRUCTIONS[OpcodeConst.FMUL] = FMUL;
        INSTRUCTIONS[OpcodeConst.DMUL] = DMUL;
        INSTRUCTIONS[OpcodeConst.IDIV] = IDIV;
        INSTRUCTIONS[OpcodeConst.LDIV] = LDIV;
        INSTRUCTIONS[OpcodeConst.FDIV] = FDIV;
        INSTRUCTIONS[OpcodeConst.DDIV] = DDIV;
        INSTRUCTIONS[OpcodeConst.IREM] = IREM;
        INSTRUCTIONS[OpcodeConst.LREM] = LREM;
        INSTRUCTIONS[OpcodeConst.FREM] = FREM;
        INSTRUCTIONS[OpcodeConst.DREM] = DREM;
        INSTRUCTIONS[OpcodeConst.INEG] = INEG;
        INSTRUCTIONS[OpcodeConst.LNEG] = LNEG;
        INSTRUCTIONS[OpcodeConst.FNEG] = FNEG;
        INSTRUCTIONS[OpcodeConst.DNEG] = DNEG;
        INSTRUCTIONS[OpcodeConst.ISHL] = ISHL;
        INSTRUCTIONS[OpcodeConst.LSHL] = LSHL;
        INSTRUCTIONS[OpcodeConst.ISHR] = ISHR;
        INSTRUCTIONS[OpcodeConst.LSHR] = LSHR;
        INSTRUCTIONS[OpcodeConst.IUSHR] = IUSHR;
        INSTRUCTIONS[OpcodeConst.LUSHR] = LUSHR;
        INSTRUCTIONS[OpcodeConst.IAND] = IAND;
        INSTRUCTIONS[OpcodeConst.LAND] = LAND;
        INSTRUCTIONS[OpcodeConst.IOR] = IOR;
        INSTRUCTIONS[OpcodeConst.LOR] = LOR;
        INSTRUCTIONS[OpcodeConst.IXOR] = IXOR;
        INSTRUCTIONS[OpcodeConst.LXOR] = LXOR;
        INSTRUCTIONS[OpcodeConst.I2L] = I2L;
        INSTRUCTIONS[OpcodeConst.I2F] = I2F;
        INSTRUCTIONS[OpcodeConst.I2D] = I2D;
        INSTRUCTIONS[OpcodeConst.L2I] = L2I;
        INSTRUCTIONS[OpcodeConst.L2F] = L2F;
        INSTRUCTIONS[OpcodeConst.L2D] = L2D;
        INSTRUCTIONS[OpcodeConst.F2I] = F2I;
        INSTRUCTIONS[OpcodeConst.F2L] = F2L;
        INSTRUCTIONS[OpcodeConst.F2D] = F2D;
        INSTRUCTIONS[OpcodeConst.D2I] = D2I;
        INSTRUCTIONS[OpcodeConst.D2L] = D2L;
        INSTRUCTIONS[OpcodeConst.D2F] = D2F;
        INSTRUCTIONS[OpcodeConst.I2B] = I2B;
        INSTRUCTIONS[OpcodeConst.I2C] = I2C;
        INSTRUCTIONS[OpcodeConst.I2S] = I2S;
        INSTRUCTIONS[OpcodeConst.LCMP] = LCMP;
        INSTRUCTIONS[OpcodeConst.FCMPL] = FCMPL;
        INSTRUCTIONS[OpcodeConst.FCMPG] = FCMPG;
        INSTRUCTIONS[OpcodeConst.DCMPL] = DCMPL;
        INSTRUCTIONS[OpcodeConst.DCMPG] = DCMPG;
        INSTRUCTIONS[OpcodeConst.IRETURN] = IRETURN;
        INSTRUCTIONS[OpcodeConst.LRETURN] = LRETURN;
        INSTRUCTIONS[OpcodeConst.FRETURN] = FRETURN;
        INSTRUCTIONS[OpcodeConst.DRETURN] = DRETURN;
        INSTRUCTIONS[OpcodeConst.ARETURN] = ARETURN;
        INSTRUCTIONS[OpcodeConst.RETURN] = RETURN;
        INSTRUCTIONS[OpcodeConst.ARRAYLENGTH] = ARRAYLENGTH;
        INSTRUCTIONS[OpcodeConst.ATHROW] = ATHROW;
        INSTRUCTIONS[OpcodeConst.MONITORENTER] = MONITORENTER;
        INSTRUCTIONS[OpcodeConst.MONITOREXIT] = MONITOREXIT;
    }

    private InstructionConst() { } // non-instantiable

    /**
     * Gets the Instruction.
     * @param index the index, e.g. {@link OpcodeConst#RETURN}
     * @return the entry from the private INSTRUCTIONS table
     */
    public static Instruction getInstruction(final int index) {
        return INSTRUCTIONS[index];
    }
}
