package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to an int object.
 *
 * @see     Constant
 */
public final class ConstantInteger extends Constant {
    private int bytes;

    /**
     * @param bytes Data
     */
    public ConstantInteger(final int bytes) {
        super(CPConst.CONSTANT_Integer);
        this.bytes = bytes;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantInteger(final DataInput file) throws IOException {
        this(file.readInt());
    }

    /**
     * @return data, i.e., 4 bytes.
     */
    public final int getBytes() {
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
        v.visitConstantInteger(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return super.toString() + "(bytes = " + bytes + ")";
    }
}
