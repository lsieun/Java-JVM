package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
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
}
