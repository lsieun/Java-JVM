package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for the xRETURN family of instructions.
 *
 */
public abstract class ReturnInstruction extends Instruction implements TypedInstruction, StackConsumer {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    ReturnInstruction() {
    }


    /**
     * @param opcode of instruction
     */
    protected ReturnInstruction(final short opcode) {
        super(opcode, (short) 1);
    }


    public Type getType() {
        final short _opcode = super.getOpcode();
        switch (_opcode) {
            case OpcodeConst.IRETURN:
                return Type.INT;
            case OpcodeConst.LRETURN:
                return Type.LONG;
            case OpcodeConst.FRETURN:
                return Type.FLOAT;
            case OpcodeConst.DRETURN:
                return Type.DOUBLE;
            case OpcodeConst.ARETURN:
                return Type.OBJECT;
            case OpcodeConst.RETURN:
                return Type.VOID;
            default: // Never reached
                throw new ClassGenException("Unknown type " + _opcode);
        }
    }


    /**
     * @return type associated with the instruction
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return getType();
    }
}
