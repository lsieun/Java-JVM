package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Visitor;

public final class ConstantUtf8 extends Constant {
    private final String bytes;

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantUtf8(final DataInput file) throws IOException {
        super(CPConst.CONSTANT_Utf8);
        bytes = file.readUTF();
    }

    /**
     * @return Data converted to string.
     */
    public final String getBytes() {
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
        v.visitConstantUtf8(this);
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString() + "(\"" + bytes + "\")";
    }
}
