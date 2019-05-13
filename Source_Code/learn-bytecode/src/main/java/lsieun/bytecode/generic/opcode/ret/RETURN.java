package lsieun.bytecode.generic.opcode.ret;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ReturnInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * RETURN -  Return from void method
 * <PRE>Stack: ... -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id$
 */
public class RETURN extends ReturnInstruction {

    public RETURN() {
        super(OpcodeConst.RETURN);
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
        v.visitRETURN(this);
    }
}
