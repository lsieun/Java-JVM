package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Visitor;

/**
 * This class represents an entry in the exception table of the <em>Code</em>
 * attribute and is used only there. It contains a range in which a
 * particular exception handler is active.
 *
 * @see     Code
 */
public final class CodeException implements Node {
    private int start_pc; // Range in the code the exception handler is
    private int end_pc; // active. start_pc is inclusive, end_pc exclusive
    /**
     * Starting address of exception handler, i.e.,
     * an offset from start of code.
     */
    private int handler_pc;

    /**
     * If this is zero the handler catches any
     * exception, otherwise it points to the
     * exception class which is to be caught.
     */
    private int catch_type;

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     */
    CodeException(final DataInput file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file
                .readUnsignedShort());
    }

    /**
     * @param start_pc Range in the code the exception handler is active,
     * start_pc is inclusive while
     * @param end_pc is exclusive
     * @param handler_pc Starting address of exception handler, i.e.,
     * an offset from start of code.
     * @param catch_type If zero the handler catches any
     * exception, otherwise it points to the exception class which is
     * to be caught.
     */
    public CodeException(final int start_pc, final int end_pc, final int handler_pc, final int catch_type) {
        this.start_pc = start_pc;
        this.end_pc = end_pc;
        this.handler_pc = handler_pc;
        this.catch_type = catch_type;
    }

    /**
     * @return Inclusive start index of the region where the handler is active.
     */
    public final int getStartPC() {
        return start_pc;
    }

    /**
     * @return Exclusive end index of the region where the handler is active.
     */
    public final int getEndPC() {
        return end_pc;
    }

    /**
     * @return Starting address of exception handler, relative to the code.
     */
    public final int getHandlerPC() {
        return handler_pc;
    }

    /**
     * @return 0, if the handler catches any exception, otherwise it points to
     * the exception class which is to be caught.
     */
    public final int getCatchType() {
        return catch_type;
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
        v.visitCodeException(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return "CodeException(start_pc = " + start_pc + ", end_pc = " + end_pc + ", handler_pc = "
                + handler_pc + ", catch_type = " + catch_type + ")";
    }
}
