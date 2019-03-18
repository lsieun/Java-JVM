package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class is derived from <em>Attribute</em> and denotes that this is a
 * deprecated method.
 * It is instantiated from the <em>Attribute.readAttribute()</em> method.
 *
 * @see     Attribute
 */
public final class Deprecated extends Attribute {
    private byte[] bytes;

    /**
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param bytes Attribute contents
     * @param constant_pool Array of constants
     */
    public Deprecated(final int name_index, final int length, final byte[] bytes, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_DEPRECATED, name_index, length, constant_pool);
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
    Deprecated(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, (byte[]) null, constant_pool);
        if (length > 0) {
            bytes = new byte[length];
            input.readFully(bytes);
            System.err.println("Deprecated attribute with length > 0");
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
        v.visitDeprecated(this);
    }

    /**
     * @return attribute name
     */
    @Override
    public final String toString() {
        return AttrConst.getAttributeName(AttrConst.ATTR_DEPRECATED);
    }
}
