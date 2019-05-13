package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * SIPUSH - Push short
 *
 * <PRE>Stack: ... -&gt; ..., value</PRE>
 */
public class SIPUSH extends ConstantPushInstruction {

    private short shortValue;


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public SIPUSH() {
    }


    public SIPUSH(final short shortValue) {
        super(OpcodeConst.SIPUSH, (short) 3);
        this.shortValue = shortValue;
    }


    @Override
    public Number getValue() {
        return Integer.valueOf(shortValue);
    }

    /**
     * @return Type.SHORT
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.SHORT;
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        super.setLength(3);
        shortValue = byteDashboard.readShort();
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
        v.visitPushInstruction(this);
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitConstantPushInstruction(this);
        v.visitSIPUSH(this);
    }
}
