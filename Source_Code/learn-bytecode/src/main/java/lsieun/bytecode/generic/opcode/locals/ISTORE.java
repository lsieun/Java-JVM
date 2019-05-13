package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.load.StoreInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * ISTORE - Store int from stack into local variable
 * <PRE>Stack: ..., value -&gt; ... </PRE>
 *
 * @version $Id$
 */
public class ISTORE extends StoreInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public ISTORE() {
        super(OpcodeConst.ISTORE, OpcodeConst.ISTORE_0);
    }


    /**
     * Store int into local variable
     *
     * @param n index of local variable
     */
    public ISTORE(final int n) {
        super(OpcodeConst.ISTORE, OpcodeConst.ISTORE_0, n);
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
        super.accept(v);
        v.visitISTORE(this);
    }
}
