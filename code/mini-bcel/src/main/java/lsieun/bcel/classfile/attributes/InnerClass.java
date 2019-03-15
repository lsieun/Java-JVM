package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * This class represents a inner class attribute, i.e., the class
 * indices of the inner and outer classes, the name and the attributes
 * of the inner class.
 *
 * @see InnerClasses
 */
public final class InnerClass implements Node {
    private int inner_class_index;
    private int outer_class_index;
    private int inner_name_index;
    private int inner_access_flags;

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     */
    InnerClass(final DataInput file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file
                .readUnsignedShort());
    }


    /**
     * @param inner_class_index Class index in constant pool of inner class
     * @param outer_class_index Class index in constant pool of outer class
     * @param inner_name_index  Name index in constant pool of inner class
     * @param inner_access_flags Access flags of inner class
     */
    public InnerClass(final int inner_class_index, final int outer_class_index, final int inner_name_index,
                      final int inner_access_flags) {
        this.inner_class_index = inner_class_index;
        this.outer_class_index = outer_class_index;
        this.inner_name_index = inner_name_index;
        this.inner_access_flags = inner_access_flags;
    }

    /**
     * @return class index of inner class.
     */
    public final int getInnerClassIndex() {
        return inner_class_index;
    }

    /**
     * @return class index of outer class.
     */
    public final int getOuterClassIndex() {
        return outer_class_index;
    }

    /**
     * @return name index of inner class.
     */
    public final int getInnerNameIndex() {
        return inner_name_index;
    }

    /**
     * @return access flags of inner class.
     */
    public final int getInnerAccessFlags() {
        return inner_access_flags;
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
        v.visitInnerClass(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return "InnerClass(" + inner_class_index + ", " + outer_class_index + ", "
                + inner_name_index + ", " + inner_access_flags + ")";
    }

    /**
     * @return Resolved string representation
     */
    public final String toString(final ConstantPool constant_pool) {
        String outer_class_name;
        String inner_name;
        String inner_class_name = constant_pool.getConstantString(inner_class_index,
                CPConst.CONSTANT_Class);
        inner_class_name = Utility.compactClassName(inner_class_name);
        if (outer_class_index != 0) {
            outer_class_name = constant_pool.getConstantString(outer_class_index,
                    CPConst.CONSTANT_Class);
            outer_class_name = " of class " + Utility.compactClassName(outer_class_name);
        } else {
            outer_class_name = "";
        }
        if (inner_name_index != 0) {
            inner_name = ((ConstantUtf8) constant_pool.getConstant(inner_name_index,
                    CPConst.CONSTANT_Utf8)).getBytes();
        } else {
            inner_name = "(anonymous)";
        }
        String access = Utility.accessToString(inner_access_flags, true);
        access = access.isEmpty() ? "" : (access + " ");
        return "  " + access + inner_name + "=class " + inner_class_name + outer_class_name;
    }

}
