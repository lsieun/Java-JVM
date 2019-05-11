package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.StoreInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * ASTORE - Store reference into local variable
 * <PRE>Stack ..., objectref -&gt; ... </PRE>
 *
 * @version $Id$
 */
public class ASTORE extends StoreInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public ASTORE() {
        super(OpcodeConst.ASTORE, OpcodeConst.ASTORE_0);
    }


    /**
     * Store reference into local variable
     *
     * @param n index of local variable
     */
    public ASTORE(final int n) {
        super(OpcodeConst.ASTORE, OpcodeConst.ASTORE_0, n);
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
        v.visitASTORE(this);
    }
}
