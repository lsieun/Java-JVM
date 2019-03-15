package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a (external) class.
 *
 * @see     Constant
 */
public final class ConstantClass extends Constant {
    // Identical to ConstantString except for the name
    private int name_index;

    /**
     * @param name_index Name index in constant pool.  Should refer to a
     * ConstantUtf8.
     */
    public ConstantClass(final int name_index) {
        super(CPConst.CONSTANT_Class);
        this.name_index = name_index;
    }

    /**
     * Constructs an instance from file data.
     *
     * @param dataInput Input stream
     * @throws IOException if an I/O error occurs reading from the given {@code dataInput}.
     */
    ConstantClass(final DataInput dataInput) throws IOException {
        this(dataInput.readUnsignedShort());
    }

    /**
     * @return Name index in constant pool of class name.
     */
    public final int getNameIndex() {
        return name_index;
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
        v.visitConstantClass(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return super.toString() + "(name_index = " + name_index + ")";
    }
}
