package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;

/**
 * This class is derived from the abstract  {@link Constant}
 * and represents a reference to a Double object.
 *
 * @see     Constant
 */
public final class ConstantDouble extends Constant {
    private double bytes;

    /**
     * @param bytes Data
     */
    public ConstantDouble(final double bytes) {
        super(CPConst.CONSTANT_Double);
        this.bytes = bytes;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantDouble(final DataInput file) throws IOException {
        this(file.readDouble());
    }

    /**
     * @return data, i.e., 8 bytes.
     */
    public final double getBytes() {
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
        v.visitConstantDouble(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return super.toString() + "(bytes = " + bytes + ")";
    }
}
