package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a float object.
 *
 * @see     Constant
 */
public final class ConstantFloat extends Constant {
    private float bytes;

    /**
     * @param bytes Data
     */
    public ConstantFloat(final float bytes) {
        super(CPConst.CONSTANT_Float);
        this.bytes = bytes;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantFloat(final DataInput file) throws IOException {
        this(file.readFloat());
    }

    /**
     * @return data, i.e., 4 bytes.
     */
    public final float getBytes() {
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
        v.visitConstantFloat(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return super.toString() + "(bytes = " + bytes + ")";
    }
}
