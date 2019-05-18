package lsieun.bytecode.generic.opcode.invoke;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.cp.InvokeInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * INVOKEINTERFACE - Invoke interface method
 * <PRE>Stack: ..., objectref, [arg1, [arg2 ...]] -&gt; ...</PRE>
 */
public final class INVOKEINTERFACE extends InvokeInstruction {
    private int nargs; // Number of arguments on stack (number of stack slots), called "count" in vmspec2


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public INVOKEINTERFACE() {
    }


    public INVOKEINTERFACE(final int index, final int nargs) {
        super(OpcodeConst.INVOKEINTERFACE, index);
        super.setLength(5);
        if (nargs < 1) {
            throw new ClassGenException("Number of arguments must be > 0 " + nargs);
        }
        this.nargs = nargs;
    }

    /**
     * The <B>count</B> argument according to the Java Language Specification,
     * Second Edition.
     */
    public int getCount() {
        return nargs;
    }

    @Override
    public int consumeStack(final ConstantPoolGen cpg) { // nargs is given in byte-code
        return nargs; // nargs includes this reference
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        super.readFully(byteDashboard, wide);
        super.setLength(5);
        nargs = byteDashboard.nextByte();
        byteDashboard.readByte(); // Skip 0 byte
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
        v.visitINVOKEINTERFACE(this);
    }
}
