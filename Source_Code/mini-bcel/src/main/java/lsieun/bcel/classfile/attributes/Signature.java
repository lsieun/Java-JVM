package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * This class is derived from <em>Attribute</em> and represents a reference
 * to a GJ attribute.
 *
 * @see     Attribute
 */
public final class Signature extends Attribute {
    private int signature_index;

    /**
     * Construct object from file stream.
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    Signature(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, input.readUnsignedShort(), constant_pool);
    }


    /**
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param signature_index Index in constant pool to CONSTANT_Utf8
     * @param constant_pool Array of constants
     */
    public Signature(final int name_index, final int length, final int signature_index, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_SIGNATURE, name_index, length, constant_pool);
        this.signature_index = signature_index;
    }

    /**
     * @return Index in constant pool of source file name.
     */
    public final int getSignatureIndex() {
        return signature_index;
    }

    /**
     * @return GJ signature.
     */
    public final String getSignature() {
        final ConstantUtf8 c = (ConstantUtf8) super.getConstantPool().getConstant(signature_index, CPConst.CONSTANT_Utf8);
        return c.getBytes();
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
        //System.err.println("Visiting non-standard Signature object");
        v.visitSignature(this);
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        final String s = getSignature();
        return "Signature: " + s;
    }
}
