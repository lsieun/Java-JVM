package lsieun.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.attributes.Attribute;
import lsieun.bcel.classfile.attributes.ConstantValue;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.exceptions.ClassFormatException;

/**
 * This class represents the field info structure, i.e., the representation
 * for a variable in the class. See JVM specification for details.
 *
 */
public final class Field extends FieldOrMethod {

    /**
     * Construct object from file stream.
     * @param file Input stream
     */
    public Field(final DataInput file, final ConstantPool constant_pool) throws IOException, ClassFormatException {
        super(file, constant_pool);
    }

    /**
     * @param access_flags Access rights of field
     * @param name_index Points to field name in constant pool
     * @param signature_index Points to encoded signature
     * @param attributes Collection of attributes
     * @param constant_pool Array of constants
     */
    public Field(final int access_flags, final int name_index, final int signature_index, final Attribute[] attributes,
                 final ConstantPool constant_pool) {
        super(access_flags, name_index, signature_index, attributes, constant_pool);
    }

    /**
     * @return constant value associated with this field (may be null)
     */
    public final ConstantValue getConstantValue() {
        for (final Attribute attribute : super.getAttributes()) {
            if (attribute.getTag() == AttrConst.ATTR_CONSTANT_VALUE) {
                return (ConstantValue) attribute;
            }
        }
        return null;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( final Visitor v ) {
        v.visitField(this);
    }

    /**
     * Return string representation close to declaration format,
     * `public static final short MAX = 100', e.g..
     *
     * @return String representation of field, including the signature.
     */
    @Override
    public final String toString() {
        String name;
        String signature;
        String access; // Short cuts to constant pool

        // Get names from constant pool
        access = Utility.accessToString(super.getAccessFlags());
        access = access.isEmpty() ? "" : (access + " ");
        signature = Utility.signatureToString(getSignature());
        name = getName();
        final StringBuilder buf = new StringBuilder(64); // CHECKSTYLE IGNORE MagicNumber
        buf.append(access).append(signature).append(" ").append(name);
        final ConstantValue cv = getConstantValue();
        if (cv != null) {
            buf.append(" = ").append(cv);
        }
        for (final Attribute attribute : super.getAttributes()) {
            if (!(attribute instanceof ConstantValue)) {
                buf.append(" [").append(attribute).append("]");
            }
        }
        return buf.toString();
    }
}
