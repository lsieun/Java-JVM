package lsieun.bytecode.generic.opcode.array;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ArrayInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;

/**
 * FASTORE -  Store into float array
 * <PRE>Stack: ..., arrayref, index, value -&gt; ...</PRE>
 */
public class FASTORE extends ArrayInstruction implements StackConsumer {

    /**
     * Store float into array
     */
    public FASTORE() {
        super(OpcodeConst.FASTORE);
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
        v.visitTypedInstruction(this);
        v.visitArrayInstruction(this);
        v.visitFASTORE(this);
    }
}
