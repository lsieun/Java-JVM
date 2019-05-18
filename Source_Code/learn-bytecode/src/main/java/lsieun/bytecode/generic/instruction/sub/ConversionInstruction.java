package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for the x2y family of instructions.
 */
public abstract class ConversionInstruction extends Instruction implements TypedInstruction,
        StackProducer, StackConsumer {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    ConversionInstruction() {
    }


    /**
     * @param opcode opcode of instruction
     */
    protected ConversionInstruction(final short opcode) {
        super(opcode, (short) 1);
    }


    /**
     * @return type associated with the instruction
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        final short opcode = super.getOpcode();
        switch (opcode) {
            case OpcodeConst.D2I:
            case OpcodeConst.F2I:
            case OpcodeConst.L2I:
                return Type.INT;
            case OpcodeConst.D2F:
            case OpcodeConst.I2F:
            case OpcodeConst.L2F:
                return Type.FLOAT;
            case OpcodeConst.D2L:
            case OpcodeConst.F2L:
            case OpcodeConst.I2L:
                return Type.LONG;
            case OpcodeConst.F2D:
            case OpcodeConst.I2D:
            case OpcodeConst.L2D:
                return Type.DOUBLE;
            case OpcodeConst.I2B:
                return Type.BYTE;
            case OpcodeConst.I2C:
                return Type.CHAR;
            case OpcodeConst.I2S:
                return Type.SHORT;
            default: // Never reached
                throw new ClassGenException("Unknown type " + opcode);
        }
    }
}
