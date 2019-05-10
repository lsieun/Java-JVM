package lsieun.bytecode.generic.opcode.stack;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.PushInstruction;
import lsieun.bytecode.generic.instruction.StackInstruction;

/**
 * DUP2 - Duplicate two top operand stack words
 * <PRE>Stack: ..., word2, word1 -&gt; ..., word2, word1, word2, word1</PRE>
 */
public class DUP2 extends StackInstruction implements PushInstruction {

    public DUP2() {
        super(OpcodeConst.DUP2);
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
        v.visitPushInstruction(this);
        v.visitStackInstruction(this);
        v.visitDUP2(this);
    }
}
