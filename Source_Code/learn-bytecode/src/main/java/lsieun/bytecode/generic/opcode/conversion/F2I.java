package lsieun.bytecode.generic.opcode.conversion;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * F2I - Convert float to int
 * <PRE>Stack: ..., value -&gt; ..., result</PRE>
 *
 * @version $Id$
 */
public class F2I extends ConversionInstruction {

    /**
     * Convert float to int
     */
    public F2I() {
        super(OpcodeConst.F2I);
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
        v.visitStackProducer(this);
        v.visitStackConsumer(this);
        v.visitConversionInstruction(this);
        v.visitF2I(this);
    }
}
