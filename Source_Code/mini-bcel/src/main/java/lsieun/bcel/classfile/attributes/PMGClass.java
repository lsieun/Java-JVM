package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * This class is derived from <em>Attribute</em> and represents a reference
 * to a PMG attribute.
 *
 * @see     Attribute
 */
public final class PMGClass extends Attribute {
    private int pmg_class_index;
    private int pmg_index;

    /**
     * Construct object from input stream.
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    PMGClass(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, input.readUnsignedShort(), input.readUnsignedShort(), constant_pool);
    }


    /**
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param pmg_index index in constant pool for source file name
     * @param pmg_class_index Index in constant pool to CONSTANT_Utf8
     * @param constant_pool Array of constants
     */
    public PMGClass(final int name_index, final int length, final int pmg_index, final int pmg_class_index,
                    final ConstantPool constant_pool) {
        super(AttrConst.ATTR_PMG, name_index, length, constant_pool);
        this.pmg_index = pmg_index;
        this.pmg_class_index = pmg_class_index;
    }

    /**
     * @return Index in constant pool of source file name.
     */
    public final int getPMGClassIndex() {
        return pmg_class_index;
    }

    /**
     * @return Index in constant pool of source file name.
     */
    public final int getPMGIndex() {
        return pmg_index;
    }

    /**
     * @return PMG name.
     */
    public final String getPMGName() {
        final ConstantUtf8 c = (ConstantUtf8) super.getConstantPool().getConstant(pmg_index, CPConst.CONSTANT_Utf8);
        return c.getBytes();
    }

    /**
     * @return PMG class name.
     */
    public final String getPMGClassName() {
        final ConstantUtf8 c = (ConstantUtf8) super.getConstantPool().getConstant(pmg_class_index,
                CPConst.CONSTANT_Utf8);
        return c.getBytes();
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
        System.err.println("Visiting non-standard PMGClass object");
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return "PMGClass(" + getPMGName() + ", " + getPMGClassName() + ")";
    }
}
