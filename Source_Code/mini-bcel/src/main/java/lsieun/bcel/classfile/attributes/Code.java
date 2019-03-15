package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class represents a chunk of Java byte code contained in a
 * method. It is instantiated by the
 * <em>Attribute.readAttribute()</em> method. A <em>Code</em>
 * attribute contains informations about operand stack, local
 * variables, byte code and the exceptions handled within this
 * method.
 *
 * This attribute has attributes itself, namely <em>LineNumberTable</em> which
 * is used for debugging purposes and <em>LocalVariableTable</em> which
 * contains information about the local variables.
 *
 * @version $Id$
 * @see     Attribute
 * @see     CodeException
 * @see     LineNumberTable
 * @see LocalVariableTable
 */
public final class Code extends Attribute {
    private final int max_stack; // Maximum size of stack used by this method
    private final int max_locals; // Number of local variables
    private byte[] code; // Actual byte code
    private CodeException[] exception_table; // Table of handled exceptions
    private Attribute[] attributes; // or LocalVariable

    /**
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param file Input stream
     * @param constant_pool Array of constants
     */
    Code(final int name_index, final int length, final DataInput file, final ConstantPool constant_pool)
            throws IOException {
        // Initialize with some default values which will be overwritten later
        this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), (byte[]) null,
                (CodeException[]) null, (Attribute[]) null, constant_pool);
        final int code_length = file.readInt();
        code = new byte[code_length]; // Read byte code
        // TODO: 这是我要整理的一个方法，我找它好久了呢readFully
        file.readFully(code);
        /* Read exception table that contains all regions where an exception
         * handler is active, i.e., a try { ... } catch() block.
         */
        final int exception_table_length = file.readUnsignedShort();
        exception_table = new CodeException[exception_table_length];
        for (int i = 0; i < exception_table_length; i++) {
            exception_table[i] = new CodeException(file);
        }
        /* Read all attributes, currently `LineNumberTable' and
         * `LocalVariableTable'
         */
        final int attributes_count = file.readUnsignedShort();
        attributes = new Attribute[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = Attribute.readAttribute(file, constant_pool);
        }
        /* Adjust length, because of setAttributes in this(), s.b.  length
         * is incorrect, because it didn't take the internal attributes
         * into account yet! Very subtle bug, fixed in 3.1.1.
         */
        // FIXME: 以后执行程序的时候，注意一下这个length到底是怎么回事呢？
        super.setLength(length);
    }

    /**
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param max_stack Maximum size of stack
     * @param max_locals Number of local variables
     * @param code Actual byte code
     * @param exception_table Table of handled exceptions
     * @param attributes Attributes of code: LineNumber or LocalVariable
     * @param constant_pool Array of constants
     */
    public Code(final int name_index, final int length, final int max_stack, final int max_locals, final byte[] code,
                final CodeException[] exception_table, final Attribute[] attributes, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_CODE, name_index, length, constant_pool);
        this.max_stack = max_stack;
        this.max_locals = max_locals;
        this.code = code != null ? code : new byte[0];
        this.exception_table = exception_table != null ? exception_table : new CodeException[0];
        this.attributes = attributes != null ? attributes : new Attribute[0];
        super.setLength(calculateLength()); // Adjust length
    }

    /**
     * @return Maximum size of stack used by this method.
     */
    public final int getMaxStack() {
        return max_stack;
    }

    /**
     * @return Number of local variables.
     */
    public final int getMaxLocals() {
        return max_locals;
    }

    /**
     * @return Actual byte code of the method.
     */
    public final byte[] getCode() {
        return code;
    }

    /**
     * @return Table of handled exceptions.
     * @see CodeException
     */
    public final CodeException[] getExceptionTable() {
        return exception_table;
    }

    /**
     * @return Collection of code attributes.
     * @see Attribute
     */
    public final Attribute[] getAttributes() {
        return attributes;
    }

    /**
     * @return LineNumberTable of Code, if it has one
     */
    public LineNumberTable getLineNumberTable() {
        for (final Attribute attribute : attributes) {
            if (attribute instanceof LineNumberTable) {
                return (LineNumberTable) attribute;
            }
        }
        return null;
    }

    /**
     * @return LocalVariableTable of Code, if it has one
     */
    public LocalVariableTable getLocalVariableTable() {
        for (final Attribute attribute : attributes) {
            if (attribute instanceof LocalVariableTable) {
                return (LocalVariableTable) attribute;
            }
        }
        return null;
    }

    /**
     * @return the full size of this code attribute, minus its first 6 bytes,
     * including the size of all its contained attributes
     */
    private int calculateLength() {
        int len = 0;
        if (attributes != null) {
            for (final Attribute attribute : attributes) {
                len += attribute.getLength() + 6 /*attribute header size*/;
            }
        }
        return len + getInternalLength();
    }

    /**
     * @return the internal length of this code attribute (minus the first 6 bytes)
     * and excluding all its attributes
     */
    private int getInternalLength() {
        return 2 /*max_stack*/+ 2 /*max_locals*/+ 4 /*code length*/
                + code.length /*byte-code*/
                + 2 /*exception-table length*/
                + 8 * (exception_table == null ? 0 : exception_table.length) /* exception table */
                + 2 /* attributes count */;
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
        v.visitCode(this);
    }

    /**
     * @return String representation of code chunk.
     */
    @Override
    public final String toString() {
        return toString(true);
    }

    /**
     * @return String representation of code chunk.
     */
    public final String toString( final boolean verbose ) {
        final StringBuilder buf = new StringBuilder(100); // CHECKSTYLE IGNORE MagicNumber
        buf.append("Code(max_stack = ").append(max_stack).append(", max_locals = ").append(
                max_locals).append(", code_length = ").append(code.length).append(")\n").append(
                Utility.codeToString(code, super.getConstantPool(), 0, -1, verbose));
        if (exception_table.length > 0) {
            buf.append("\nException handler(s) = \n").append("From\tTo\tHandler\tType\n");
            for (final CodeException exception : exception_table) {
                buf.append(exception.toString(super.getConstantPool(), verbose)).append("\n");
            }
        }
        if (attributes.length > 0) {
            buf.append("\nAttribute(s) = ");
            for (final Attribute attribute : attributes) {
                buf.append("\n").append(attribute);
            }
        }
        return buf.toString();
    }
}
