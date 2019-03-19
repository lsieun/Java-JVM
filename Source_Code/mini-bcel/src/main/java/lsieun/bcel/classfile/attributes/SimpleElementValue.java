package lsieun.bcel.classfile.attributes;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantDouble;
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

    public double getValueDouble() {
        if (super.getType() != PRIMITIVE_DOUBLE) {
            throw new RuntimeException("Dont call getValueDouble() on a non DOUBLE ElementValue");
        }
        final ConstantDouble d = (ConstantDouble) super.getConstantPool().getConstant(getIndex());
        return d.getBytes();
    }

    public boolean getValueBoolean() {
        if (super.getType() != PRIMITIVE_BOOLEAN) {
            throw new RuntimeException("Dont call getValueBoolean() on a non BOOLEAN ElementValue");
        }
        final ConstantInteger bo = (ConstantInteger) super.getConstantPool().getConstant(getIndex());
        return bo.getBytes() != 0;
    }

    public short getValueShort() {
        if (super.getType() != PRIMITIVE_SHORT) {
            throw new RuntimeException("Dont call getValueShort() on a non SHORT ElementValue");
        }
        final ConstantInteger s = (ConstantInteger) super.getConstantPool().getConstant(getIndex());
        return (short) s.getBytes();
    }

    @Override
    public String toString() {
        return stringifyValue();
    }

    // Whatever kind of value it is, return it as a string
    @Override
    public String stringifyValue() {
        final ConstantPool cpool = super.getConstantPool();
        final int _type = super.getType();
        switch (_type)
        {
            case PRIMITIVE_INT:
                final ConstantInteger c = (ConstantInteger) cpool.getConstant(getIndex(), CPConst.CONSTANT_Integer);
                return Integer.toString(c.getBytes());
            case PRIMITIVE_LONG:
                final ConstantLong j = (ConstantLong) cpool.getConstant(getIndex(), CPConst.CONSTANT_Long);
                return Long.toString(j.getBytes());
            case PRIMITIVE_DOUBLE:
                final ConstantDouble d = (ConstantDouble) cpool.getConstant(getIndex(), CPConst.CONSTANT_Double);
                return Double.toString(d.getBytes());
            case PRIMITIVE_FLOAT:
                final ConstantFloat f = (ConstantFloat) cpool.getConstant(getIndex(), CPConst.CONSTANT_Float);
                return Float.toString(f.getBytes());
            case PRIMITIVE_SHORT:
                final ConstantInteger s = (ConstantInteger) cpool.getConstant(getIndex(), CPConst.CONSTANT_Integer);
                return Integer.toString(s.getBytes());
            case PRIMITIVE_BYTE:
                final ConstantInteger b = (ConstantInteger) cpool.getConstant(getIndex(), CPConst.CONSTANT_Integer);
                return Integer.toString(b.getBytes());
            case PRIMITIVE_CHAR:
                final ConstantInteger ch = (ConstantInteger) cpool.getConstant(getIndex(), CPConst.CONSTANT_Integer);
                return String.valueOf((char)ch.getBytes());
            case PRIMITIVE_BOOLEAN:
                final ConstantInteger bo = (ConstantInteger) cpool.getConstant(getIndex(), CPConst.CONSTANT_Integer);
                if (bo.getBytes() == 0) {
                    return "false";
                }
                return "true";
            case STRING:
                final ConstantUtf8 cu8 = (ConstantUtf8) cpool.getConstant(getIndex(), CPConst.CONSTANT_Utf8);
                return cu8.getBytes();
            default:
                throw new RuntimeException("SimpleElementValue class does not know how to stringify type " + _type);
        }
    }
}
