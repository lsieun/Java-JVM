package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * ARRAYLENGTH -  Get length of array
 * <PRE>Stack: ..., arrayref -&gt; ..., length</PRE>
 */
public class ARRAYLENGTH extends Instruction implements StackProducer, StackConsumer {

    /**
     * Get length of array
     */
    public ARRAYLENGTH() {
        super(OpcodeConst.ARRAYLENGTH, (short) 1);
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
        v.visitARRAYLENGTH(this);
    }
}
