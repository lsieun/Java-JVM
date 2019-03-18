package lsieun.bcel.classfile.attributes;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantFloat;
import lsieun.bcel.classfile.cp.ConstantInteger;
import lsieun.bcel.classfile.cp.ConstantLong;
import lsieun.bcel.classfile.cp.ConstantUtf8;

public class SimpleElementValue extends ElementValue {
    private int index;

    public SimpleElementValue(final int type, final int index, final ConstantPool cpool) {
        super(type, cpool);
        this.index = index;
    }

    /**
     * @return Value entry index in the cpool
     */
    public int getIndex() {
        return index;
    }

    public String getValueString() {
        if (super.getType() != STRING) {
            throw new RuntimeException("Dont call getValueString() on a non STRING ElementValue");
        }
        final ConstantUtf8 c = (ConstantUtf8) super.getConstantPool().getConstant(getIndex(), CPConst.CONSTANT_Utf8);
        return c.getBytes();
    }

    public int getValueInt() {
        if (super.getType() != PRIMITIVE_INT) {
            throw new RuntimeException("Dont call getValueString() on a non STRING ElementValue");
        }
        final ConstantInteger c = (ConstantInteger) super.getConstantPool().getConstant(getIndex(), CPConst.CONSTANT_Integer);
        return c.getBytes();
    }

    public byte getValueByte() {
        if (super.getType() != PRIMITIVE_BYTE) {
            throw new RuntimeException("Dont call getValueByte() on a non BYTE ElementValue");
        }
        final ConstantInteger c = (ConstantInteger) super.getConstantPool().getConstant(getIndex(), CPConst.CONSTANT_Integer);
        return (byte) c.getBytes();
    }

    public char getValueChar() {
        if (super.getType() != PRIMITIVE_CHAR) {
            throw new RuntimeException("Dont call getValueChar() on a non CHAR ElementValue");
        }
        final ConstantInteger c = (ConstantInteger) super.getConstantPool().getConstant(getIndex(), CPConst.CONSTANT_Integer);
        return (char) c.getBytes();
    }

    public long getValueLong() {
        if (super.getType() != PRIMITIVE_LONG) {
            throw new RuntimeException("Dont call getValueLong() on a non LONG ElementValue");
        }
        final ConstantLong j = (ConstantLong) super.getConstantPool().getConstant(getIndex());
        return j.getBytes();
    }

    public float getValueFloat() {
        if (super.getType() != PRIMITIVE_FLOAT) {
            throw new RuntimeException("Dont call getValueFloat() on a non FLOAT ElementValue");
        }
        final ConstantFloat f = (ConstantFloat) super.getConstantPool().getConstant(getIndex());
        return f.getBytes();
    }
}
