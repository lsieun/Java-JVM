package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class represents a table of line numbers for debugging
 * purposes. This attribute is used by the <em>Code</em> attribute. It
 * contains pairs of PCs and line numbers.
 *
 * @see     Code
 * @see LineNumber
 */
public final class LineNumberTable extends Attribute {
    private static final int MAX_LINE_LENGTH = 72;
    private LineNumber[] line_number_table; // Table of line/numbers pairs

    /*
     * @param name_index Index of name
     * @param length Content length in bytes
     * @param line_number_table Table of line/numbers pairs
     * @param constant_pool Array of constants
     */
    public LineNumberTable(final int name_index, final int length, final LineNumber[] line_number_table,
                           final ConstantPool constant_pool) {
        super(AttrConst.ATTR_LINE_NUMBER_TABLE, name_index, length, constant_pool);
        this.line_number_table = line_number_table;
    }

    /**
     * Construct object from input stream.
     * @param name_index Index of name
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException if an I/O Exception occurs in readUnsignedShort
     */
    LineNumberTable(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, (LineNumber[]) null, constant_pool);
        final int line_number_table_length = input.readUnsignedShort();
        line_number_table = new LineNumber[line_number_table_length];
        for (int i = 0; i < line_number_table_length; i++) {
            line_number_table[i] = new LineNumber(input);
        }
    }

    /**
     * @return Array of (pc offset, line number) pairs.
     */
    public final LineNumber[] getLineNumberTable() {
        return line_number_table;
    }

    public final int getTableLength() {
        return line_number_table == null ? 0 : line_number_table.length;
    }

    // TODO: getSourceLine这个方法，我要进行测试，感觉很有用
    /**
     * Map byte code positions to source code lines.
     *
     * @param pos byte code offset
     * @return corresponding line in source code
     */
    public int getSourceLine( final int pos ) {
        int l = 0;
        int r = line_number_table.length - 1;
        if (r < 0) {
            return -1;
        }
        int min_index = -1;
        int min = -1;
        /* Do a binary search since the array is ordered.
         */
        do {
            final int i = (l + r) / 2;
            final int j = line_number_table[i].getStartPC();
            if (j == pos) {
                return line_number_table[i].getLineNumber();
            } else if (pos < j) {
                r = i - 1;
            } else {
                l = i + 1;
            }
            /* If exact match can't be found (which is the most common case)
             * return the line number that corresponds to the greatest index less
             * than pos.
             */
            if (j < pos && j > min) {
                min = j;
                min_index = i;
            }
        } while (l <= r);
        /* It's possible that we did not find any valid entry for the bytecode
         * offset we were looking for.
         */
        if (min_index < 0) {
            return -1;
        }
        return line_number_table[min_index].getLineNumber();
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
        v.visitLineNumberTable(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder();
        final StringBuilder line = new StringBuilder();
        final String newLine = System.getProperty("line.separator", "\n");
        for (int i = 0; i < line_number_table.length; i++) {
            line.append(line_number_table[i].toString());
            if (i < line_number_table.length - 1) {
                line.append(", ");
            }
            if ((line.length() > MAX_LINE_LENGTH) && (i < line_number_table.length - 1)) {
                line.append(newLine);
                buf.append(line);
                line.setLength(0);
            }
        }
        buf.append(line);
        return buf.toString();
    }
}
