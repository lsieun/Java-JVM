package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * MONITORENTER - Enter monitor for object
 * <PRE>Stack: ..., objectref -&gt; ...</PRE>
 *
 * @version $Id$
 */
public class MONITORENTER extends Instruction implements StackConsumer {

    public MONITORENTER() {
        super(OpcodeConst.MONITORENTER, (short) 1);
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
        v.visitMONITORENTER(this);
    }
}
