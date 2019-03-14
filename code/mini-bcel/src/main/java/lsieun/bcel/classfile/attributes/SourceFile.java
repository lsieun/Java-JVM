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
 * to the source file of this class.  At most one SourceFile attribute
 * should appear per classfile.  The intention of this class is that it is
 * instantiated from the <em>Attribute.readAttribute()</em> method.
 *
 * @see     Attribute
 */
public final class SourceFile extends Attribute {
    private int sourcefile_index;

    /**
     * Construct object from input stream.
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    SourceFile(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, input.readUnsignedShort(), constant_pool);
    }

    /**
     * @param name_index Index in constant pool to CONSTANT_Utf8, which
     * should represent the string "SourceFile".
     * @param length Content length in bytes, the value should be 2.
     * @param constant_pool The constant pool that this attribute is
     * associated with.
     * @param sourcefile_index Index in constant pool to CONSTANT_Utf8.  This
     * string will be interpreted as the name of the file from which this
     * class was compiled.  It will not be interpreted as indicating the name
     * of the directory containing the file or an absolute path; this
     * information has to be supplied the consumer of this attribute - in
     * many cases, the JVM.
     */
    public SourceFile(final int name_index, final int length, final int sourcefile_index, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_SOURCE_FILE, name_index, length, constant_pool);
        this.sourcefile_index = sourcefile_index;
    }

    /**
     * @return Index in constant pool of source file name.
     */
    public final int getSourceFileIndex() {
        return sourcefile_index;
    }

    /**
     * @return Source file name.
     */
    public final String getSourceFileName() {
        final ConstantUtf8 c = (ConstantUtf8) super.getConstantPool().getConstant(sourcefile_index,
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
        v.visitSourceFile(this);
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return "SourceFile: " + getSourceFileName();
    }
}
