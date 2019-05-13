package lsieun.bytecode.generic.opcode.conversion;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * F2D - Convert float to double
 * <PRE>Stack: ..., value -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id$
 */
public class F2D extends ConversionInstruction {

    /**
     * Convert float to double
     */
    public F2D() {
        super(OpcodeConst.F2D);
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
        v.visitF2D(this);
    }
}
