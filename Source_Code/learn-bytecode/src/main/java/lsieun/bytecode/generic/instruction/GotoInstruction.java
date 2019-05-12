package lsieun.bytecode.generic.instruction;

/**
 * Super class for GOTO
 *
 * @version $Id$
 */
public abstract class GotoInstruction extends BranchInstruction implements UnconditionalBranch {

    public GotoInstruction(final short opcode, final InstructionHandle target) {
        super(opcode, target);
    }


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public GotoInstruction() {
    }
}
