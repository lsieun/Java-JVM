package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a long object.
 *
 * @see     Constant
 */
public final class ConstantLong extends Constant {
    private long bytes;

    /**
     * @param bytes Data
     */
    public ConstantLong(final long bytes) {
        super(CPConst.CONSTANT_Long);
        this.bytes = bytes;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantLong(final DataInput file) throws IOException {
        this(file.readLong());
    }

    /**
     * @return data, i.e., 8 bytes.
     */
    public final long getBytes() {
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
        v.visitConstantLong(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return super.toString() + "(bytes = " + bytes + ")";
    }
}
