package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.load.LoadInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * LLOAD - Load long from local variable
 * <PRE>Stack ... -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id$
 */
public class LLOAD extends LoadInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public LLOAD() {
        super(OpcodeConst.LLOAD, OpcodeConst.LLOAD_0);
    }


    public LLOAD(final int n) {
        super(OpcodeConst.LLOAD, OpcodeConst.LLOAD_0, n);
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
        v.visitLLOAD(this);
    }
}
