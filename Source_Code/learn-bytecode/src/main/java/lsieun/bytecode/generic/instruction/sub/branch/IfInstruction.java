package lsieun.bytecode.generic.instruction.sub.branch;

import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;

/**
 * Super class for the IFxxx family of instructions.
 */
public abstract class IfInstruction extends BranchInstruction implements StackConsumer {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public IfInstruction() {
    }


    /**
     * @param opcode opcode of instruction
     * @param target Target instruction to branch to
     */
    protected IfInstruction(final short opcode, final InstructionHandle target) {
        super(opcode, target);
    }


    /**
     * @return negation of instruction, e.g. IFEQ.negate() == IFNE
     */
    public abstract IfInstruction negate();
}
