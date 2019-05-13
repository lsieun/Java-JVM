package lsieun.bytecode.generic.opcode.arithmetic;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ArithmeticInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * IREM - Remainder of int
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id$
 */
public class IREM extends ArithmeticInstruction {

    /** Remainder of ints
     */
    public IREM() {
        super(OpcodeConst.IREM);
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
    public void accept( final Visitor v ) {
        v.visitTypedInstruction(this);
        v.visitStackProducer(this);
        v.visitStackConsumer(this);
        v.visitArithmeticInstruction(this);
        v.visitIREM(this);
    }
}
