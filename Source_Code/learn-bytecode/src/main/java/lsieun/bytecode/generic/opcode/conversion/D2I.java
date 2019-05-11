package lsieun.bytecode.generic.opcode.conversion;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConversionInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * D2I - Convert double to int
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result</PRE>
 *
 * @version $Id$
 */
public class D2I extends ConversionInstruction {

    /**
     * Convert double to int
     */
    public D2I() {
        super(OpcodeConst.D2I);
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
        v.visitD2I(this);
    }
}
