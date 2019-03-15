package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class represents a constant pool reference to a field.
 *
 */
public final class ConstantFieldref extends ConstantCP {
    /**
     * Initialize instance from input data.
     *
     * @param input input stream
     * @throws IOException
     */
    ConstantFieldref(final DataInput input) throws IOException {
        super(CPConst.CONSTANT_Fieldref, input);
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of Fields,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( final Visitor v) {
        v.visitConstantFieldref(this);
    }
}
