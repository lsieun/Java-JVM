package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class represents colection of local variables in a
 * method. This attribute is contained in the <em>Code</em> attribute.
 *
 * @see     Code
 * @see LocalVariable
 */
public final class LocalVariableTable extends Attribute {
    private LocalVariable[] local_variable_table; // variables

    /**
     * @param name_index Index in constant pool to `LocalVariableTable'
     * @param length Content length in bytes
     * @param local_variable_table Table of local variables
     * @param constant_pool Array of constants
     */
    public LocalVariableTable(final int name_index, final int length, final LocalVariable[] local_variable_table,
                              final ConstantPool constant_pool) {
        super(AttrConst.ATTR_LOCAL_VARIABLE_TABLE, name_index, length, constant_pool);
        this.local_variable_table = local_variable_table;
    }

    /**
     * Construct object from input stream.
     * @param name_index Index in constant pool
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    LocalVariableTable(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        this(name_index, length, (LocalVariable[]) null, constant_pool);
        final int local_variable_table_length = input.readUnsignedShort();
        local_variable_table = new LocalVariable[local_variable_table_length];
        for (int i = 0; i < local_variable_table_length; i++) {
            local_variable_table[i] = new LocalVariable(input, constant_pool);
        }
    }

    /**
     * @return Array of local variables of method.
     */
    public final LocalVariable[] getLocalVariableTable() {
        return local_variable_table;
    }

    public final int getTableLength() {
        return local_variable_table == null ? 0 : local_variable_table.length;
    }

    /**
     *
     * @param index the variable slot
     * @param pc the current pc that this variable is alive
     *
     * @return the LocalVariable that matches or null if not found
     */
    public final LocalVariable getLocalVariable( final int index, final int pc ) {
        for (final LocalVariable variable : local_variable_table) {
            if (variable.getIndex() == index) {
                final int start_pc = variable.getStartPC();
                final int end_pc = start_pc + variable.getLength();
                if ((pc >= start_pc) && (pc <= end_pc)) {
                    return variable;
                }
            }
        }
        return null;
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
        v.visitLocalVariableTable(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < local_variable_table.length; i++) {
            buf.append(local_variable_table[i]);
            if (i < local_variable_table.length - 1) {
                buf.append('\n');
            }
        }
        return buf.toString();
    }
}
