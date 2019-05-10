package lsieun.bytecode.generic.opcode.stack;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.PopInstruction;
import lsieun.bytecode.generic.instruction.StackInstruction;

/**
 * POP2 - Pop two top operand stack words
 *
 * <PRE>Stack: ..., word2, word1 -&gt; ...</PRE>
 */
public class POP2 extends StackInstruction implements PopInstruction {

    public POP2() {
        super(OpcodeConst.POP2);
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
        v.visitPopInstruction(this);
        v.visitStackInstruction(this);
        v.visitPOP2(this);
    }
}
