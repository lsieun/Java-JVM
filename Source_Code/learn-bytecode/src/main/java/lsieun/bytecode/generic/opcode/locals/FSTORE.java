package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.StoreInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * FSTORE - Store float into local variable
 * <PRE>Stack: ..., value -&gt; ... </PRE>
 *
 * @version $Id$
 */
public class FSTORE extends StoreInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public FSTORE() {
        super(OpcodeConst.FSTORE, OpcodeConst.FSTORE_0);
    }


    /**
     * Store float into local variable
     *
     * @param n index of local variable
     */
    public FSTORE(final int n) {
        super(OpcodeConst.FSTORE, OpcodeConst.FSTORE_0, n);
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
        v.visitFSTORE(this);
    }
}
