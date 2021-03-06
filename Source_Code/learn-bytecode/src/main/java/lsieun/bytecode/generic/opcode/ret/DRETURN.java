package lsieun.bytecode.generic.opcode.ret;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ReturnInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * DRETURN -  Return double from method
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id$
 */
public class DRETURN extends ReturnInstruction {

    /**
     * Return double from method
     */
    public DRETURN() {
        super(OpcodeConst.DRETURN);
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
        v.visitTypedInstruction(this);
        v.visitStackConsumer(this);
        v.visitReturnInstruction(this);
        v.visitDRETURN(this);
    }
}
