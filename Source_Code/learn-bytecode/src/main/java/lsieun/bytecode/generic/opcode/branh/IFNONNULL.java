package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.branch.IfInstruction;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * IFNONNULL - Branch if reference is not null
 *
 * <PRE>Stack: ..., reference -&gt; ...</PRE>
 *
 */
public class IFNONNULL extends IfInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public IFNONNULL() {
    }


    public IFNONNULL(final InstructionHandle target) {
        super(OpcodeConst.IFNONNULL, target);
    }


    /**
     * @return negation of instruction
     */
    @Override
    public IfInstruction negate() {
        return new IFNULL(super.getTarget());
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitStackConsumer(this);
        v.visitBranchInstruction(this);
        v.visitIfInstruction(this);
        v.visitIFNONNULL(this);
    }
}
