package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.classfile.cp.Constant;
import lsieun.bytecode.classfile.cp.ConstantClass;
import lsieun.bytecode.classfile.cp.ConstantFloat;
import lsieun.bytecode.classfile.cp.ConstantInteger;
import lsieun.bytecode.classfile.cp.ConstantString;
import lsieun.bytecode.classfile.cp.ConstantUtf8;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.cst.JVMConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.facet.PushInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * LDC - Push item from constant pool.
 *
 * <PRE>Stack: ... -&gt; ..., item</PRE>
 */
public class LDC extends CPInstruction implements PushInstruction {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public LDC() {
    }


    public LDC(final int index) {
        super(OpcodeConst.LDC_W, index);
        setSize();
    }

    // Adjust to proper size
    protected final void setSize() {
        if (super.getIndex() <= JVMConst.MAX_BYTE) { // Fits in one byte?
            super.setOpcode(OpcodeConst.LDC);
            super.setLength(2);
        } else {
            super.setOpcode(OpcodeConst.LDC_W);
            super.setLength(3);
        }
    }

    /**
     * Set the index to constant pool and adjust size.
     */
    @Override
    public final void setIndex(final int index) {
        super.setIndex(index);
        setSize();
    }


    public Object getValue(final ConstantPoolGen cpg) {
        Constant c = cpg.getConstantPool().getConstant(super.getIndex());
        switch (c.getTag()) {
            case CPConst.CONSTANT_String:
                final int i = ((ConstantString) c).getStringIndex();
                c = cpg.getConstantPool().getConstant(i);
                return ((ConstantUtf8) c).getUtf8Value();
            case CPConst.CONSTANT_Float:
                return new Float(((ConstantFloat) c).getFloatValue());
            case CPConst.CONSTANT_Integer:
                return Integer.valueOf(((ConstantInteger) c).getIntValue());
            case CPConst.CONSTANT_Class:
                final int nameIndex = ((ConstantClass) c).getNameIndex();
                c = cpg.getConstantPool().getConstant(nameIndex);
                return new ObjectType(((ConstantUtf8) c).getUtf8Value());
            default: // Never reached
                throw new RuntimeException("Unknown or invalid constant type at " + super.getIndex());
        }
    }

    @Override
    public Type getType(final ConstantPoolGen cpg) {
        switch (cpg.getConstantPool().getConstant(super.getIndex()).getTag()) {
            case CPConst.CONSTANT_String:
                return Type.STRING;
            case CPConst.CONSTANT_Float:
                return Type.FLOAT;
            case CPConst.CONSTANT_Integer:
                return Type.INT;
            case CPConst.CONSTANT_Class:
                return Type.CLASS;
            default: // Never reached
                throw new RuntimeException("Unknown or invalid constant type at " + super.getIndex());
        }
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        super.setLength(2);
        int cpIndex = byteDashboard.nextByte();
        super.setIndex(cpIndex);
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
        v.visitTypedInstruction(this);
        v.visitCPInstruction(this);
        v.visitLDC(this);
    }
}
