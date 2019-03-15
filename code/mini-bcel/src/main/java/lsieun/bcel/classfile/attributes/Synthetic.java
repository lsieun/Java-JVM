package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class is derived from <em>Attribute</em> and declares this class as
 * `synthetic', i.e., it needs special handling.  The JVM specification
 * states "A class member that does not appear in the source code must be
 * marked using a Synthetic attribute."  It may appear in the ClassFile
 * attribute table, a field_info table or a method_info table.  This class
 * is intended to be instantiated from the
 * <em>Attribute.readAttribute()</em> method.
 *
 * @see     Attribute
 */
public final class Synthetic extends Attribute {
    private byte[] bytes;

    /**
     * @param name_index Index in constant pool to CONSTANT_Utf8, which
     * should represent the string "Synthetic".
     * @param length Content length in bytes - should be zero.
     * @param bytes Attribute contents
     * @param constant_pool The constant pool this attribute is associated
     * with.
     */
    public Synthetic(final int name_index, final int length, final byte[] bytes, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_SYNTHETIC, name_index, length, constant_pool);
        this.bytes = bytes;
    }


    /**
     * Construct object from input stream.
     *
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    Synthetic(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, (byte[]) null, constant_pool);
        if (length > 0) {
            bytes = new byte[length];
            input.readFully(bytes);
            System.err.println("Synthetic attribute with length > 0");
        }
    }

    /**
     * @return data bytes.
     */
    public final byte[] getBytes() {
        return bytes;
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
        v.visitSynthetic(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder("Synthetic");
        if (super.getLength() > 0) {
            buf.append(" ").append(Utility.toHexString(bytes));
        }
        return buf.toString();
    }
}
