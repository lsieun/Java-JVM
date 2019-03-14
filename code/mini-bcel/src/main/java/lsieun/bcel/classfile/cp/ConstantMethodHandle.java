package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a method handle.
 *
 * @see     Constant
 */
public final class ConstantMethodHandle extends Constant {
    private int reference_kind;
    private int reference_index;

    public ConstantMethodHandle(final int reference_kind, final int reference_index) {
        super(CPConst.CONSTANT_MethodHandle);
        this.reference_kind = reference_kind;
        this.reference_index = reference_index;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantMethodHandle(final DataInput file) throws IOException {
        this(file.readUnsignedByte(), file.readUnsignedShort());
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitly
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitConstantMethodHandle(this);
    }

    public int getReferenceKind() {
        return reference_kind;
    }

    public int getReferenceIndex() {
        return reference_index;
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString() + "(reference_kind = " + reference_kind +
                ", reference_index = " + reference_index + ")";
    }
}
