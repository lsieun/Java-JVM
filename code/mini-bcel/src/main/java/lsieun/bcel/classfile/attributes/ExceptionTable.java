package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class represents the table of exceptions that are thrown by a
 * method. This attribute may be used once per method.  The name of
 * this class is <em>ExceptionTable</em> for historical reasons; The
 * Java Virtual Machine Specification, Second Edition defines this
 * attribute using the name <em>Exceptions</em> (which is inconsistent
 * with the other classes).
 *
 * @see     Code
 */
public final class ExceptionTable extends Attribute {
    private int[] exception_index_table; // constant pool

    /**
     * @param name_index Index in constant pool
     * @param length Content length in bytes
     * @param exception_index_table Table of indices in constant pool
     * @param constant_pool Array of constants
     */
    public ExceptionTable(final int name_index, final int length, final int[] exception_index_table,
                          final ConstantPool constant_pool) {
        super(AttrConst.ATTR_EXCEPTIONS, name_index, length, constant_pool);
        this.exception_index_table = exception_index_table != null ? exception_index_table : new int[0];
    }


    /**
     * Construct object from input stream.
     * @param name_index Index in constant pool
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    ExceptionTable(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool) throws IOException {
        this(name_index, length, (int[]) null, constant_pool);
        final int number_of_exceptions = input.readUnsignedShort();
        exception_index_table = new int[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++) {
            exception_index_table[i] = input.readUnsignedShort();
        }
    }

    /**
     * @return Array of indices into constant pool of thrown exceptions.
     */
    public final int[] getExceptionIndexTable() {
        return exception_index_table;
    }

    /**
     * @return Length of exception table.
     */
    public final int getNumberOfExceptions() {
        return exception_index_table == null ? 0 : exception_index_table.length;
    }

    /**
     * @return class names of thrown exceptions
     */
    public final String[] getExceptionNames() {
        final String[] names = new String[exception_index_table.length];
        for (int i = 0; i < exception_index_table.length; i++) {
            names[i] = super.getConstantPool().getConstantString(exception_index_table[i],
                    CPConst.CONSTANT_Class).replace('/', '.');
        }
        return names;
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
        v.visitExceptionTable(this);
    }

    /**
     * @return String representation, i.e., a list of thrown exceptions.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder();
        String str;
        buf.append("Exceptions: ");
        for (int i = 0; i < exception_index_table.length; i++) {
            str = super.getConstantPool().getConstantString(exception_index_table[i], CPConst.CONSTANT_Class);
            buf.append(Utility.compactClassName(str, false));
            if (i < exception_index_table.length - 1) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }
}
