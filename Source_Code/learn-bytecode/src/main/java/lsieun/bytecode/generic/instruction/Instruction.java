package lsieun.bytecode.generic.instruction;

import java.io.IOException;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * Abstract super class for all Java byte codes.
 */
public abstract class Instruction {
    /**
     * Length of instruction in bytes
     */
    private short length = 1;

    /**
     * Opcode number
     */
    private short opcode = -1;

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public Instruction() {
    }


    public Instruction(final short opcode, final short length) {
        this.opcode = opcode;
        this.length = length;
    }

    /**
     * @return name of instruction, i.e., opcode name
     */
    public String getName() {
        return OpcodeConst.getOpcodeName(opcode);
    }

    // region getters and setters
    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public short getOpcode() {
        return opcode;
    }

    public void setOpcode(short opcode) {
        this.opcode = opcode;
    }
    // endregion

    // region Stack
    /**
     * This method also gives right results for instructions whose
     * effect on the stack depends on the constant pool entry they
     * reference.
     *  @return Number of words consumed from stack by this instruction,
     * or Constants.UNPREDICTABLE, if this can not be computed statically
     */
    public int consumeStack( final ConstantPoolGen cpg ) {
        return OpcodeConst.getConsumeStack(opcode);
    }

    /**
     * This method also gives right results for instructions whose
     * effect on the stack depends on the constant pool entry they
     * reference.
     * @return Number of words produced onto stack by this instruction,
     * or Constants.UNPREDICTABLE, if this can not be computed statically
     */
    public int produceStack( final ConstantPoolGen cpg ) {
        return OpcodeConst.getProduceStack(opcode);
    }
    // endregion

    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    public abstract void accept(Visitor v);

    /**
     * Long output format:
     * <p>
     * &lt;name of opcode&gt; "["&lt;opcode number&gt;"]"
     * "("&lt;length of instruction&gt;")"
     *
     * @param verbose long/short format switch
     * @return mnemonic for instruction
     */
    public String toString(final boolean verbose) {
        if (verbose) {
            return getName() + "[" + opcode + "](" + length + ")";
        }
        return getName();
    }

    /**
     * @return mnemonic for instruction in verbose format
     */
    @Override
    public String toString() {
        return toString(true);
    }


    /**
     * Read an instruction from (byte code) input stream and return the
     * appropiate object.
     * <p>
     * If the Instruction is defined in {@link InstructionConst}, then the
     * singleton instance is returned.
     *
     * @param byteDashboard input stream bytes
     * @return instruction object being read
     * @see InstructionConst#getInstruction(int)
     */
    public static Instruction readInstruction(final ByteDashboard byteDashboard) throws IOException {
//        boolean wide = false;
//        short opcode = (short) byteDashboard.nextByte();
//        Instruction obj = null;
//        if (opcode == OpcodeConst.WIDE) { // Read next opcode after wide byte
//            wide = true;
//            opcode = (short) byteDashboard.nextByte();
//        }
//        final Instruction instruction = InstructionOpcodeConst.getInstruction(opcode);
//        if (instruction != null) {
//            return instruction; // Used predefined immutable object, if available
//        }
//
//        switch (opcode) {
//            case OpcodeConst.BIPUSH:
//                obj = new BIPUSH();
//                break;
//            case OpcodeConst.SIPUSH:
//                obj = new SIPUSH();
//                break;
//            case OpcodeConst.LDC:
//                obj = new LDC();
//                break;
//            case OpcodeConst.LDC_W:
//                obj = new LDC_W();
//                break;
//            case OpcodeConst.LDC2_W:
//                obj = new LDC2_W();
//                break;
//            case OpcodeConst.ILOAD:
//                obj = new ILOAD();
//                break;
//            case OpcodeConst.LLOAD:
//                obj = new LLOAD();
//                break;
//            case OpcodeConst.FLOAD:
//                obj = new FLOAD();
//                break;
//            case OpcodeConst.DLOAD:
//                obj = new DLOAD();
//                break;
//            case OpcodeConst.ALOAD:
//                obj = new ALOAD();
//                break;
//            case OpcodeConst.ILOAD_0:
//                obj = new ILOAD(0);
//                break;
//            case OpcodeConst.ILOAD_1:
//                obj = new ILOAD(1);
//                break;
//            case OpcodeConst.ILOAD_2:
//                obj = new ILOAD(2);
//                break;
//            case OpcodeConst.ILOAD_3:
//                obj = new ILOAD(3);
//                break;
//            case OpcodeConst.LLOAD_0:
//                obj = new LLOAD(0);
//                break;
//            case OpcodeConst.LLOAD_1:
//                obj = new LLOAD(1);
//                break;
//            case OpcodeConst.LLOAD_2:
//                obj = new LLOAD(2);
//                break;
//            case OpcodeConst.LLOAD_3:
//                obj = new LLOAD(3);
//                break;
//            case OpcodeConst.FLOAD_0:
//                obj = new FLOAD(0);
//                break;
//            case OpcodeConst.FLOAD_1:
//                obj = new FLOAD(1);
//                break;
//            case OpcodeConst.FLOAD_2:
//                obj = new FLOAD(2);
//                break;
//            case OpcodeConst.FLOAD_3:
//                obj = new FLOAD(3);
//                break;
//            case OpcodeConst.DLOAD_0:
//                obj = new DLOAD(0);
//                break;
//            case OpcodeConst.DLOAD_1:
//                obj = new DLOAD(1);
//                break;
//            case OpcodeConst.DLOAD_2:
//                obj = new DLOAD(2);
//                break;
//            case OpcodeConst.DLOAD_3:
//                obj = new DLOAD(3);
//                break;
//            case OpcodeConst.ALOAD_0:
//                obj = new ALOAD(0);
//                break;
//            case OpcodeConst.ALOAD_1:
//                obj = new ALOAD(1);
//                break;
//            case OpcodeConst.ALOAD_2:
//                obj = new ALOAD(2);
//                break;
//            case OpcodeConst.ALOAD_3:
//                obj = new ALOAD(3);
//                break;
//            case OpcodeConst.ISTORE:
//                obj = new ISTORE();
//                break;
//            case OpcodeConst.LSTORE:
//                obj = new LSTORE();
//                break;
//            case OpcodeConst.FSTORE:
//                obj = new FSTORE();
//                break;
//            case OpcodeConst.DSTORE:
//                obj = new DSTORE();
//                break;
//            case OpcodeConst.ASTORE:
//                obj = new ASTORE();
//                break;
//            case OpcodeConst.ISTORE_0:
//                obj = new ISTORE(0);
//                break;
//            case OpcodeConst.ISTORE_1:
//                obj = new ISTORE(1);
//                break;
//            case OpcodeConst.ISTORE_2:
//                obj = new ISTORE(2);
//                break;
//            case OpcodeConst.ISTORE_3:
//                obj = new ISTORE(3);
//                break;
//            case OpcodeConst.LSTORE_0:
//                obj = new LSTORE(0);
//                break;
//            case OpcodeConst.LSTORE_1:
//                obj = new LSTORE(1);
//                break;
//            case OpcodeConst.LSTORE_2:
//                obj = new LSTORE(2);
//                break;
//            case OpcodeConst.LSTORE_3:
//                obj = new LSTORE(3);
//                break;
//            case OpcodeConst.FSTORE_0:
//                obj = new FSTORE(0);
//                break;
//            case OpcodeConst.FSTORE_1:
//                obj = new FSTORE(1);
//                break;
//            case OpcodeConst.FSTORE_2:
//                obj = new FSTORE(2);
//                break;
//            case OpcodeConst.FSTORE_3:
//                obj = new FSTORE(3);
//                break;
//            case OpcodeConst.DSTORE_0:
//                obj = new DSTORE(0);
//                break;
//            case OpcodeConst.DSTORE_1:
//                obj = new DSTORE(1);
//                break;
//            case OpcodeConst.DSTORE_2:
//                obj = new DSTORE(2);
//                break;
//            case OpcodeConst.DSTORE_3:
//                obj = new DSTORE(3);
//                break;
//            case OpcodeConst.ASTORE_0:
//                obj = new ASTORE(0);
//                break;
//            case OpcodeConst.ASTORE_1:
//                obj = new ASTORE(1);
//                break;
//            case OpcodeConst.ASTORE_2:
//                obj = new ASTORE(2);
//                break;
//            case OpcodeConst.ASTORE_3:
//                obj = new ASTORE(3);
//                break;
//            case OpcodeConst.IINC:
//                obj = new IINC();
//                break;
//            case OpcodeConst.IFEQ:
//                obj = new IFEQ();
//                break;
//            case OpcodeConst.IFNE:
//                obj = new IFNE();
//                break;
//            case OpcodeConst.IFLT:
//                obj = new IFLT();
//                break;
//            case OpcodeConst.IFGE:
//                obj = new IFGE();
//                break;
//            case OpcodeConst.IFGT:
//                obj = new IFGT();
//                break;
//            case OpcodeConst.IFLE:
//                obj = new IFLE();
//                break;
//            case OpcodeConst.IF_ICMPEQ:
//                obj = new IF_ICMPEQ();
//                break;
//            case OpcodeConst.IF_ICMPNE:
//                obj = new IF_ICMPNE();
//                break;
//            case OpcodeConst.IF_ICMPLT:
//                obj = new IF_ICMPLT();
//                break;
//            case OpcodeConst.IF_ICMPGE:
//                obj = new IF_ICMPGE();
//                break;
//            case OpcodeConst.IF_ICMPGT:
//                obj = new IF_ICMPGT();
//                break;
//            case OpcodeConst.IF_ICMPLE:
//                obj = new IF_ICMPLE();
//                break;
//            case OpcodeConst.IF_ACMPEQ:
//                obj = new IF_ACMPEQ();
//                break;
//            case OpcodeConst.IF_ACMPNE:
//                obj = new IF_ACMPNE();
//                break;
//            case OpcodeConst.GOTO:
//                obj = new GOTO();
//                break;
//            case OpcodeConst.JSR:
//                obj = new JSR();
//                break;
//            case OpcodeConst.RET:
//                obj = new RET();
//                break;
//            case OpcodeConst.TABLESWITCH:
//                obj = new TABLESWITCH();
//                break;
//            case OpcodeConst.LOOKUPSWITCH:
//                obj = new LOOKUPSWITCH();
//                break;
//            case OpcodeConst.GETSTATIC:
//                obj = new GETSTATIC();
//                break;
//            case OpcodeConst.PUTSTATIC:
//                obj = new PUTSTATIC();
//                break;
//            case OpcodeConst.GETFIELD:
//                obj = new GETFIELD();
//                break;
//            case OpcodeConst.PUTFIELD:
//                obj = new PUTFIELD();
//                break;
//            case OpcodeConst.INVOKEVIRTUAL:
//                obj = new INVOKEVIRTUAL();
//                break;
//            case OpcodeConst.INVOKESPECIAL:
//                obj = new INVOKESPECIAL();
//                break;
//            case OpcodeConst.INVOKESTATIC:
//                obj = new INVOKESTATIC();
//                break;
//            case OpcodeConst.INVOKEINTERFACE:
//                obj = new INVOKEINTERFACE();
//                break;
//            case OpcodeConst.INVOKEDYNAMIC:
//                obj = new INVOKEDYNAMIC();
//                break;
//            case OpcodeConst.NEW:
//                obj = new NEW();
//                break;
//            case OpcodeConst.NEWARRAY:
//                obj = new NEWARRAY();
//                break;
//            case OpcodeConst.ANEWARRAY:
//                obj = new ANEWARRAY();
//                break;
//            case OpcodeConst.CHECKCAST:
//                obj = new CHECKCAST();
//                break;
//            case OpcodeConst.INSTANCEOF:
//                obj = new INSTANCEOF();
//                break;
//            case OpcodeConst.MULTIANEWARRAY:
//                obj = new MULTIANEWARRAY();
//                break;
//            case OpcodeConst.IFNULL:
//                obj = new IFNULL();
//                break;
//            case OpcodeConst.IFNONNULL:
//                obj = new IFNONNULL();
//                break;
//            case OpcodeConst.GOTO_W:
//                obj = new GOTO_W();
//                break;
//            case OpcodeConst.JSR_W:
//                obj = new JSR_W();
//                break;
//            case OpcodeConst.BREAKPOINT:
//                obj = new BREAKPOINT();
//                break;
//            case OpcodeConst.IMPDEP1:
//                obj = new IMPDEP1();
//                break;
//            case OpcodeConst.IMPDEP2:
//                obj = new IMPDEP2();
//                break;
//            default:
//                throw new ClassGenException("Illegal opcode detected: " + opcode);
//
//        }
//
//        if (wide
//                && !((obj instanceof LocalVariableInstruction) || (obj instanceof IINC) || (obj instanceof RET))) {
//            throw new ClassGenException("Illegal opcode after wide: " + opcode);
//        }
//        obj.setOpcode(opcode);
//        obj.initFromFile(byteDashboard, wide); // Do further initializations, if any
//        return obj;
        return null;
    }

}
