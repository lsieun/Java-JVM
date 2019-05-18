package lsieun.bytecode.generic.opcode.field;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.cp.FieldInstruction;
import lsieun.bytecode.generic.instruction.facet.PopInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * PUTFIELD - Put field in object
 * <PRE>Stack: ..., objectref, value -&gt; ...</PRE>
 * OR
 * <PRE>Stack: ..., objectref, value.word1, value.word2 -&gt; ...</PRE>
 */
public class PUTFIELD extends FieldInstruction implements PopInstruction {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public PUTFIELD() {
    }


    public PUTFIELD(final int index) {
        super(OpcodeConst.PUTFIELD, index);
    }


    @Override
    public int consumeStack(final ConstantPoolGen cpg) {
        return getFieldSize(cpg) + 1;
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
        v.visitTypedInstruction(this);
        v.visitLoadClass(this);
        v.visitCPInstruction(this);
        v.visitFieldOrMethod(this);
        v.visitFieldInstruction(this);
        v.visitPUTFIELD(this);
    }
}
