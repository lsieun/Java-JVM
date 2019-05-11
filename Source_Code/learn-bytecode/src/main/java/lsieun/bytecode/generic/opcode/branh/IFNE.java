package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.IfInstruction;
import lsieun.bytecode.generic.instruction.InstructionHandle;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * IFNE - Branch if int comparison with zero succeeds
 *
 * <PRE>Stack: ..., value -&gt; ...</PRE>
 *
 * @version $Id$
 */
public class IFNE extends IfInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public IFNE() {
    }


    public IFNE(final InstructionHandle target) {
        super(OpcodeConst.IFNE, target);
    }


    /**
     * @return negation of instruction
     */
    @Override
    public IfInstruction negate() {
        return new IFEQ(super.getTarget());
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
        v.visitIFNE(this);
    }
}
