package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;

public final class ConstantInvokeDynamic extends ConstantCP {

    public ConstantInvokeDynamic(final int bootstrap_method_attr_index, final int name_and_type_index) {
        super(CPConst.CONSTANT_InvokeDynamic, bootstrap_method_attr_index, name_and_type_index);
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantInvokeDynamic(final DataInput file) throws IOException {
        this(file.readShort(), file.readShort());
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
        v.visitConstantInvokeDynamic(this);
    }

    /**
     * @return Reference (index) to bootstrap method this constant refers to.
     *
     * Note that this method is a functional duplicate of getClassIndex
     * for use by ConstantInvokeDynamic.
     */
    public final int getBootstrapMethodAttrIndex() {
        return super.getClassIndex();  // AKA bootstrap_method_attr_index
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString().replace("class_index", "bootstrap_method_attr_index");
    }
}
