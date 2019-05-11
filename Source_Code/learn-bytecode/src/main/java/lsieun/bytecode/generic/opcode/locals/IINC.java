package lsieun.bytecode.generic.opcode.locals;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.JVMConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.LocalVariableInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;

/**
 * IINC - Increment local variable by constant
 */
public class IINC extends LocalVariableInstruction {

    private boolean wide;
    private int c;


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public IINC() {
    }


    /**
     * @param n index of local variable
     * @param c increment factor
     */
    public IINC(final int n, final int c) {
        super(); // Default behaviour of LocalVariableInstruction causes error
        super.setOpcode(OpcodeConst.IINC);
        super.setLength((short) 3);
        setIndex(n); // May set wide as side effect
        setIncrement(c);
    }


    private void setWide() {
        wide = super.getIndex() > JVMConst.MAX_BYTE;
        if (c > 0) {
            wide = wide || (c > Byte.MAX_VALUE);
        } else {
            wide = wide || (c < Byte.MIN_VALUE);
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
    public final void setIndex( final int n ) {
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
        return c;
    }


    /**
     * Set increment factor.
     */
    public final void setIncrement( final int c ) {
        this.c = c;
        setWide();
    }


    /** @return int type
     */
    @Override
    public Type getType(final ConstantPoolGen cp ) {
        return Type.INT;
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
    public void accept( final Visitor v ) {
        v.visitLocalVariableInstruction(this);
        v.visitIINC(this);
    }
}
