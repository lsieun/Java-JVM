package lsieun.bytecode.generic.opcode.invoke;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.cp.InvokeInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * INVOKESTATIC - Invoke a class (static) method
 *
 * <PRE>Stack: ..., [arg1, [arg2 ...]] -&gt; ...</PRE>
 */
public class INVOKESTATIC extends InvokeInstruction {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public INVOKESTATIC() {
    }


    public INVOKESTATIC(final int index) {
        super(OpcodeConst.INVOKESTATIC, index);
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
        v.visitTypedInstruction(this);
        v.visitStackConsumer(this);
        v.visitStackProducer(this);
        v.visitLoadClass(this);
        v.visitCPInstruction(this);
        v.visitFieldOrMethod(this);
        v.visitInvokeInstruction(this);
        v.visitINVOKESTATIC(this);
    }
}
