package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * BIPUSH - Push byte on stack
 *
 * <PRE>Stack: ... -&gt; ..., value</PRE>
 */
public class BIPUSH extends ConstantPushInstruction {
    private byte b;


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public BIPUSH() {
    }


    /**
     * Push byte on stack
     */
    public BIPUSH(final byte b) {
        super(OpcodeConst.BIPUSH, (short) 2);
        this.b = b;
    }


    @Override
    public Number getValue() {
        return Integer.valueOf(b);
    }


    /**
     * @return Type.BYTE
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.BYTE;
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        super.setLength(2);
        b = byteDashboard.readByte();
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
        v.visitBIPUSH(this);
    }
}
