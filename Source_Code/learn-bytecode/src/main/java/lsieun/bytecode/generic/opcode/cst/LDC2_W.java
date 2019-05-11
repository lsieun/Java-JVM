package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.classfile.cp.Constant;
import lsieun.bytecode.classfile.cp.ConstantDouble;
import lsieun.bytecode.classfile.cp.ConstantLong;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.CPInstruction;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.PushInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;

/**
 * LDC2_W - Push long or double from constant pool
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 * @version $Id$
 */
public class LDC2_W extends CPInstruction implements PushInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public LDC2_W() {
    }


    public LDC2_W(final int index) {
        super(OpcodeConst.LDC2_W, index);
    }


    @Override
    public Type getType(final ConstantPoolGen cpg) {
        switch (cpg.getConstantPool().getConstant(super.getIndex()).getTag()) {
            case CPConst.CONSTANT_Long:
                return Type.LONG;
            case CPConst.CONSTANT_Double:
                return Type.DOUBLE;
            default: // Never reached
                throw new RuntimeException("Unknown constant type " + super.getOpcode());
        }
    }


    public Number getValue(final ConstantPoolGen cpg) {
        final Constant c = cpg.getConstantPool().getConstant(super.getIndex());
        switch (c.getTag()) {
            case CPConst.CONSTANT_Long:
                return Long.valueOf(((ConstantLong) c).getLongValue());
            case CPConst.CONSTANT_Double:
                return new Double(((ConstantDouble) c).getDoubleValue());
            default: // Never reached
                throw new RuntimeException("Unknown or invalid constant type at " + super.getIndex());
        }
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
        v.visitLDC2_W(this);
    }
}
