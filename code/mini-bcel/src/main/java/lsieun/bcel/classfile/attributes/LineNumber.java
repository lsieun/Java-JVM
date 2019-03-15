package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Visitor;

public final class LineNumber implements Node {
    // TODO: 这个里的pc代表Program Counter，需要以后记录的事情
    /** Program Counter (PC) corresponds to line */
    private short start_pc;

    /** number in source file */
    private short line_number;

    /**
     * Construct object from file stream.
     *
     * @param file Input stream
     * @throws IOException if an I/O Exception occurs in readUnsignedShort
     */
    LineNumber(final DataInput file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }

    /**
     * @param start_pc Program Counter (PC) corresponds to
     * @param line_number line number in source file
     */
    public LineNumber(final int start_pc, final int line_number) {
        this.start_pc = (short) start_pc;
        this.line_number = (short)line_number;
    }

    /**
     * @return PC in code
     */
    public final int getStartPC() {
        return  0xffff & start_pc;
    }

    /**
     * @return Corresponding source line
     */
    public final int getLineNumber() {
        return 0xffff & line_number;
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
        v.visitLineNumber(this);
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return "LineNumber(" + start_pc + ", " + line_number + ")";
    }
}
