package lsieun.bytecode.generic.instruction.sub.branch;

import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.facet.UnconditionalBranch;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;

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
