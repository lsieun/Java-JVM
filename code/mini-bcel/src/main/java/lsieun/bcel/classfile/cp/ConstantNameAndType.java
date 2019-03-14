package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to the name and signature
 * of a field or method.
 *
 * @see     Constant
 */
public final class ConstantNameAndType extends Constant {
    private int name_index; // Name of field/method
    private int signature_index; // and its signature.

    /**
     * @param name_index Name of field/method
     * @param signature_index and its signature
     */
    public ConstantNameAndType(final int name_index, final int signature_index) {
        super(CPConst.CONSTANT_NameAndType);
        this.name_index = name_index;
        this.signature_index = signature_index;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantNameAndType(final DataInput file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }

    /**
     * @return Name index in constant pool of field/method name.
     */
    public final int getNameIndex() {
        return name_index;
    }

    /**
     * @return Index in constant pool of field/method signature.
     */
    public final int getSignatureIndex() {
        return signature_index;
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
        v.visitConstantNameAndType(this);
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString() + "(name_index = " + name_index + ", signature_index = "
                + signature_index + ")";
    }
}
