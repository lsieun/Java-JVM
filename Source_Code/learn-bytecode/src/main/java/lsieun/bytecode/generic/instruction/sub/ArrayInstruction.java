package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.fairydust.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for instructions dealing with array access such as IALOAD.
 *
 */
public abstract class ArrayInstruction extends Instruction implements TypedInstruction {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    ArrayInstruction() {
    }

    /**
     * @param opcode of instruction
     */
    protected ArrayInstruction(final short opcode) {
        super(opcode, (short) 1);
    }

    /** @return type associated with the instruction
     */
    @Override
    public Type getType(final ConstantPoolGen cp ) {
        final short _opcode = super.getOpcode();
        switch (_opcode) {
            case OpcodeConst.IALOAD:
            case OpcodeConst.IASTORE:
                return Type.INT;
            case OpcodeConst.CALOAD:
            case OpcodeConst.CASTORE:
                return Type.CHAR;
            case OpcodeConst.BALOAD:
            case OpcodeConst.BASTORE:
                return Type.BYTE;
            case OpcodeConst.SALOAD:
            case OpcodeConst.SASTORE:
                return Type.SHORT;
            case OpcodeConst.LALOAD:
            case OpcodeConst.LASTORE:
                return Type.LONG;
            case OpcodeConst.DALOAD:
            case OpcodeConst.DASTORE:
                return Type.DOUBLE;
            case OpcodeConst.FALOAD:
            case OpcodeConst.FASTORE:
                return Type.FLOAT;
            case OpcodeConst.AALOAD:
            case OpcodeConst.AASTORE:
                return Type.OBJECT;
            default:
                throw new ClassGenException("Oops: unknown case in switch" + _opcode);
        }
    }
}
