package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a method type.
 *
 * @see     Constant
 */
public final class ConstantMethodType extends Constant {
    private int descriptor_index;

    public ConstantMethodType(final int descriptor_index) {
        super(CPConst.CONSTANT_MethodType);
        this.descriptor_index = descriptor_index;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantMethodType(final DataInput file) throws IOException {
        this(file.readUnsignedShort());
    }

    public int getDescriptorIndex() {
        return descriptor_index;
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
        v.visitConstantMethodType(this);
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString() + "(descriptor_index = " + descriptor_index + ")";
    }
}
