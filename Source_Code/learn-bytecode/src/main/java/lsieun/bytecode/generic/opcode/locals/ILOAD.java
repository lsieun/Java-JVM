package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.LoadInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * ILOAD - Load int from local variable onto stack
 * <PRE>Stack: ... -&gt; ..., result</PRE>
 */
public class ILOAD extends LoadInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public ILOAD() {
        super(OpcodeConst.ILOAD, OpcodeConst.ILOAD_0);
    }


    /**
     * Load int from local variable
     *
     * @param n index of local variable
     */
    public ILOAD(final int n) {
        super(OpcodeConst.ILOAD, OpcodeConst.ILOAD_0, n);
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
        v.visitILOAD(this);
    }
}
