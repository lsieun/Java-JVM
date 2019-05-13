package lsieun.bytecode.generic.opcode.invoke;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.cp.InvokeInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * INVOKESPECIAL - Invoke instance method; special handling for superclass, private
 * and instance initialization method invocations
 *
 * <PRE>Stack: ..., objectref, [arg1, [arg2 ...]] -&gt; ...</PRE>
 */
public class INVOKESPECIAL extends InvokeInstruction {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public INVOKESPECIAL() {
    }


    public INVOKESPECIAL(final int index) {
        super(OpcodeConst.INVOKESPECIAL, index);
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
        v.visitINVOKESPECIAL(this);
    }
}
