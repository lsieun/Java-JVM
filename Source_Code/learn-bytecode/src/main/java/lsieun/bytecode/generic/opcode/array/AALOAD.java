package lsieun.bytecode.generic.opcode.array;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ArrayInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.facet.StackProducer;

/**
 * AALOAD - Load reference from array
 * <PRE>Stack: ..., arrayref, index -&gt; value</PRE>
 *
 */
public class AALOAD extends ArrayInstruction implements StackProducer {

    /** Load reference from array
     */
    public AALOAD() {
        super(OpcodeConst.AALOAD);
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
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitArrayInstruction(this);
        v.visitAALOAD(this);
    }
}