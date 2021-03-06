package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a module.
 *
 * <p>Note: Early access Java 9 support- currently subject to change</p>
 *
 * @see     Constant
 */
public final class ConstantModule extends Constant {

    private int name_index;

    /**
     * @param name_index Name index in constant pool.  Should refer to a
     * ConstantUtf8.
     */
    public ConstantModule(final int name_index) {
        super(CPConst.CONSTANT_Module);
        this.name_index = name_index;
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantModule(final DataInput file) throws IOException {
        this(file.readUnsignedShort());
    }

    /**
     * @return Name index in constant pool of module name.
     */
    public final int getNameIndex() {
        return name_index;
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
        v.visitConstantModule(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return super.toString() + "(name_index = " + name_index + ")";
    }
}
