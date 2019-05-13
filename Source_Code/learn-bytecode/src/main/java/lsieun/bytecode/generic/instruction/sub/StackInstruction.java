package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for stack operations like DUP and POP.
 */
public abstract class StackInstruction extends Instruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    StackInstruction() {
    }


    /**
     * @param opcode instruction opcode
     */
    protected StackInstruction(final short opcode) {
        super(opcode, (short) 1);
    }


    /**
     * @return Type.UNKNOWN
     */
    public Type getType(final ConstantPoolGen cp) {
        return Type.UNKNOWN;
    }
}

