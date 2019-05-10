package lsieun.bytecode.generic.instruction;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for the family of arithmetic instructions.
 *
 */
public abstract class ArithmeticInstruction extends Instruction implements TypedInstruction,
        StackProducer, StackConsumer {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    ArithmeticInstruction() {
    }


    /**
     * @param opcode of instruction
     */
    protected ArithmeticInstruction(final short opcode) {
        super(opcode, (short) 1);
    }


    /**
     * @return type associated with the instruction
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        final short _opcode = super.getOpcode();
        switch (_opcode) {
            case OpcodeConst.DADD:
            case OpcodeConst.DDIV:
            case OpcodeConst.DMUL:
            case OpcodeConst.DNEG:
            case OpcodeConst.DREM:
            case OpcodeConst.DSUB:
                return Type.DOUBLE;
            case OpcodeConst.FADD:
            case OpcodeConst.FDIV:
            case OpcodeConst.FMUL:
            case OpcodeConst.FNEG:
            case OpcodeConst.FREM:
            case OpcodeConst.FSUB:
                return Type.FLOAT;
            case OpcodeConst.IADD:
            case OpcodeConst.IAND:
            case OpcodeConst.IDIV:
            case OpcodeConst.IMUL:
            case OpcodeConst.INEG:
            case OpcodeConst.IOR:
            case OpcodeConst.IREM:
            case OpcodeConst.ISHL:
            case OpcodeConst.ISHR:
            case OpcodeConst.ISUB:
            case OpcodeConst.IUSHR:
            case OpcodeConst.IXOR:
                return Type.INT;
            case OpcodeConst.LADD:
            case OpcodeConst.LAND:
            case OpcodeConst.LDIV:
            case OpcodeConst.LMUL:
            case OpcodeConst.LNEG:
            case OpcodeConst.LOR:
            case OpcodeConst.LREM:
            case OpcodeConst.LSHL:
            case OpcodeConst.LSHR:
            case OpcodeConst.LSUB:
            case OpcodeConst.LUSHR:
            case OpcodeConst.LXOR:
                return Type.LONG;
            default: // Never reached
                throw new ClassGenException("Unknown type " + _opcode);
        }
    }
}
