package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.JVMConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.LocalVariableInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * IINC - Increment local variable by constant
 */
public class IINC extends LocalVariableInstruction {

    private boolean wide;
    private int constValue;


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public IINC() {
    }


    /**
     * @param localVarIndex index of local variable
     * @param constValue increment factor
     */
    public IINC(final int localVarIndex, final int constValue) {
        super(); // Default behaviour of LocalVariableInstruction causes error
        super.setOpcode(OpcodeConst.IINC);
        super.setLength((short) 3);
        setIndex(localVarIndex); // May set wide as side effect
        setIncrement(constValue);
    }


    private void setWide() {
        wide = super.getIndex() > JVMConst.MAX_BYTE;
        if (constValue > 0) {
            wide = wide || (constValue > Byte.MAX_VALUE);
        } else {
            wide = wide || (constValue < Byte.MIN_VALUE);
        }
        if (wide) {
            super.setLength(6); // wide byte included
        } else {
            super.setLength(3);
        }
    }

    /**
     * Set index of local variable.
     */
    @Override
    public final void setIndex(final int n) {
        if (n < 0) {
            throw new ClassGenException("Negative index value: " + n);
        }
        super.setIndexOnly(n);
        setWide();
    }


    /**
     * @return increment factor
     */
    public final int getIncrement() {
        return constValue;
    }


    /**
     * Set increment factor.
     */
    public final void setIncrement(final int constValue) {
        this.constValue = constValue;
        setWide();
    }


    /**
     * @return int type
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.INT;
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        this.wide = wide;
        if (wide) {
            super.setLength(6);
            int localIndex = byteDashboard.nextShort();
            super.setIndexOnly(localIndex);
            int incrValue = byteDashboard.readShort();
            constValue = incrValue;
        } else {
            super.setLength(3);
            int localIndex = byteDashboard.nextByte();
            super.setIndexOnly(localIndex);
            int incrValue = byteDashboard.readByte();
            constValue = incrValue;
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
        v.visitLocalVariableInstruction(this);
        v.visitIINC(this);
    }
}
