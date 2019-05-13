package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.load.LoadInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * FLOAD - Load float from local variable
 * <PRE>Stack ... -&gt; ..., result</PRE>
 *
 * @version $Id$
 */
public class FLOAD extends LoadInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public FLOAD() {
        super(OpcodeConst.FLOAD, OpcodeConst.FLOAD_0);
    }


    /**
     * Load float from local variable
     *
     * @param n index of local variable
     */
    public FLOAD(final int n) {
        super(OpcodeConst.FLOAD, OpcodeConst.FLOAD_0, n);
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
        v.visitFLOAD(this);
    }
}
