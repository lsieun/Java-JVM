package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * This class represents a reference to an unknown (i.e.,
 * application-specific) attribute of a class.  It is instantiated from the
 * {@link Attribute#readAttribute(java.io.DataInput, ConstantPool)} method.
 * Applications that need to read in application-specific attributes should create an
 * {@link UnknownAttributeReader} implementation and attach it via
 * {@link Attribute#addAttributeReader(String, UnknownAttributeReader)}.

 *
 * @version $Id$
 * @see Attribute
 * @see UnknownAttributeReader
 */
public final class Unknown extends Attribute {
    private byte[] bytes;
    private final String name;
    private static final Map<String, Unknown> unknown_attributes = new HashMap();

    /** @return array of unknown attributes, but just one for each kind.
     */
    static Unknown[] getUnknownAttributes() {
        final Unknown[] unknowns = new Unknown[unknown_attributes.size()];
        unknown_attributes.values().toArray(unknowns);
        unknown_attributes.clear();
        return unknowns;
    }

    /**
     * Create a non-standard attribute.
     *
     * @param name_index Index in constant pool
     * @param length Content length in bytes
     * @param bytes Attribute contents
     * @param constant_pool Array of constants
     */
    public Unknown(final int name_index, final int length, final byte[] bytes, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_UNKNOWN, name_index, length, constant_pool);
        this.bytes = bytes;
        name = ((ConstantUtf8) constant_pool.getConstant(name_index, CPConst.CONSTANT_Utf8)).getBytes();
        unknown_attributes.put(name, this);
    }


    /**
     * Construct object from input stream.
     *
     * @param name_index Index in constant pool
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    Unknown(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, (byte[]) null, constant_pool);
        if (length > 0) {
            bytes = new byte[length];
            input.readFully(bytes);
        }
    }

    /**
     * @return data bytes.
     */
    public final byte[] getBytes() {
        return bytes;
    }

    /**
     * @return name of attribute.
     */
    @Override
    public final String getName() {
        return name;
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
        v.visitUnknown(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        if (super.getLength() == 0 || bytes == null) {
            return "(Unknown attribute " + name + ")";
        }
        String hex;
        if (super.getLength() > 10) {
            final byte[] tmp = new byte[10];
            System.arraycopy(bytes, 0, tmp, 0, 10);
            hex = Utility.toHexString(tmp) + "... (truncated)";
        } else {
            hex = Utility.toHexString(bytes);
        }
        return "(Unknown attribute " + name + ": " + hex + ")";
    }
}
