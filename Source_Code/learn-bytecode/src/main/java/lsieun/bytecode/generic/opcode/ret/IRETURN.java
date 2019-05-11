package lsieun.bytecode.generic.opcode.ret;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ReturnInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * IRETURN -  Return int from method
 * <PRE>Stack: ..., value -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id$
 */
public class IRETURN extends ReturnInstruction {

    /**
     * Return int from method
     */
    public IRETURN() {
        super(OpcodeConst.IRETURN);
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
        v.visitIRETURN(this);
    }
}
