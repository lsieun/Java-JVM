package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.Constant;
import lsieun.bcel.classfile.cp.ConstantDouble;
import lsieun.bcel.classfile.cp.ConstantFloat;
import lsieun.bcel.classfile.cp.ConstantInteger;
import lsieun.bcel.classfile.cp.ConstantLong;
import lsieun.bcel.classfile.cp.ConstantString;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * This class is derived from <em>Attribute</em> and represents a constant
 * value, i.e., a default value for initializing a class field.
 * This class is instantiated by the <em>Attribute.readAttribute()</em> method.
 *
 * @see     Attribute
 */
public final class ConstantValue extends Attribute {
    private int constantvalue_index;

    /**
     * @param name_index Name index in constant pool
     * @param length Content length in bytes
     * @param constantvalue_index Index in constant pool
     * @param constant_pool Array of constants
     */
    public ConstantValue(final int name_index, final int length, final int constantvalue_index,
                         final ConstantPool constant_pool) {
        super(AttrConst.ATTR_CONSTANT_VALUE, name_index, length, constant_pool);
        this.constantvalue_index = constantvalue_index;
    }

    /**
     * Construct object from input stream.
     * @param name_index Name index in constant pool
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    ConstantValue(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, input.readUnsignedShort(), constant_pool);
    }

    /**
     * @return Index in constant pool of constant value.
     */
    public final int getConstantValueIndex() {
        return constantvalue_index;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitConstantValue(this);
    }

    /**
     * @return String representation of constant value.
     */
    @Override
    public final String toString() {
        Constant c = super.getConstantPool().getConstant(constantvalue_index);
        String buf;
        int i;
        // Print constant to string depending on its type
        switch (c.getTag()) {
            case CPConst.CONSTANT_Long:
                buf = String.valueOf(((ConstantLong) c).getBytes());
                break;
            case CPConst.CONSTANT_Float:
                buf = String.valueOf(((ConstantFloat) c).getBytes());
                break;
            case CPConst.CONSTANT_Double:
                buf = String.valueOf(((ConstantDouble) c).getBytes());
                break;
            case CPConst.CONSTANT_Integer:
                buf = String.valueOf(((ConstantInteger) c).getBytes());
                break;
            case CPConst.CONSTANT_String:
                i = ((ConstantString) c).getStringIndex();
                c = super.getConstantPool().getConstant(i, CPConst.CONSTANT_Utf8);
                buf = "\"" + Utility.convertString(((ConstantUtf8) c).getBytes()) + "\"";
                break;
            default:
                throw new IllegalStateException("Type of ConstValue invalid: " + c);
        }
        return buf;
    }
}
