package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class is derived from <em>Attribute</em> and denotes that this class
 * is an Inner class of another.
 * to the source file of this class.
 * It is instantiated from the <em>Attribute.readAttribute()</em> method.
 *
 * @see     Attribute
 */
public final class InnerClasses extends Attribute {
    private InnerClass[] inner_classes;

    /**
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param inner_classes array of inner classes attributes
     * @param constant_pool Array of constants
     */
    public InnerClasses(final int name_index, final int length, final InnerClass[] inner_classes,
                        final ConstantPool constant_pool) {
        super(AttrConst.ATTR_INNER_CLASSES, name_index, length, constant_pool);
        this.inner_classes = inner_classes != null ? inner_classes : new InnerClass[0];
    }


    /**
     * Construct object from input stream.
     *
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    InnerClasses(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, (InnerClass[]) null, constant_pool);
        final int number_of_classes = input.readUnsignedShort();
        inner_classes = new InnerClass[number_of_classes];
        for (int i = 0; i < number_of_classes; i++) {
            inner_classes[i] = new InnerClass(input);
        }
    }

    /**
     * @return array of inner class "records"
     */
    public final InnerClass[] getInnerClasses() {
        return inner_classes;
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
        v.visitInnerClasses(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("InnerClasses(");
        buf.append(inner_classes.length);
        buf.append("):\n");
        for (final InnerClass inner_class : inner_classes) {
            buf.append(inner_class.toString(super.getConstantPool())).append("\n");
        }
        return buf.toString();
    }
}
