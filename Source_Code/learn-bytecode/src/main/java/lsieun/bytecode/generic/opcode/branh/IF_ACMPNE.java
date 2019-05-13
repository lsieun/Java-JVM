package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.branch.IfInstruction;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * IF_ACMPNE - Branch if reference comparison doesn't succeed
 *
 * <PRE>Stack: ..., value1, value2 -&gt; ...</PRE>
 *
 * @version $Id$
 */
public class IF_ACMPNE extends IfInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public IF_ACMPNE() {
    }


    public IF_ACMPNE(final InstructionHandle target) {
        super(OpcodeConst.IF_ACMPNE, target);
    }


    /**
     * @return negation of instruction
     */
    @Override
    public IfInstruction negate() {
        return new IF_ACMPEQ(super.getTarget());
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
        v.visitIF_ACMPNE(this);
    }
}
