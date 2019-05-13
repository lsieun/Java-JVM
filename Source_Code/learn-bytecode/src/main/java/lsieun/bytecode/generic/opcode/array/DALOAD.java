package lsieun.bytecode.generic.opcode.array;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ArrayInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.facet.StackProducer;

/**
 * DALOAD - Load double from array
 * <PRE>Stack: ..., arrayref, index -&gt; ..., result.word1, result.word2</PRE>
 */
public class DALOAD extends ArrayInstruction implements StackProducer {

    /**
     * Load double from array
     */
    public DALOAD() {
        super(OpcodeConst.DALOAD);
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
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitArrayInstruction(this);
        v.visitDALOAD(this);
    }
}
