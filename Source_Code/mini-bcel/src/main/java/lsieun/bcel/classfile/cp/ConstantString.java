package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a String object.
 *
 * @see     Constant
 */
public final class ConstantString extends Constant {

    private int string_index; // Identical to ConstantClass except for this name

    /**
     * @param string_index Index of Constant_Utf8 in constant pool
     */
    public ConstantString(final int string_index) {
        super(CPConst.CONSTANT_String);
        this.string_index = string_index;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantString(final DataInput file) throws IOException {
        this(file.readUnsignedShort());
    }

    /**
     * @return Index in constant pool of the string (ConstantUtf8).
     */
    public final int getStringIndex() {
        return string_index;
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
        v.visitConstantString(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return super.toString() + "(string_index = " + string_index + ")";
    }
}
