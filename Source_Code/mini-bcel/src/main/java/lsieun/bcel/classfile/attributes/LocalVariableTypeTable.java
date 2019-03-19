package lsieun.bcel.classfile.attributes;

// The new table is used when generic types are about...

//LocalVariableTable_attribute {
//       u2 attribute_name_index;
//       u4 attribute_length;
//       u2 local_variable_table_length;
//       {  u2 start_pc;
//          u2 length;
//          u2 name_index;
//          u2 descriptor_index;
//          u2 index;
//       } local_variable_table[local_variable_table_length];
//     }

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

//LocalVariableTypeTable_attribute {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u2 local_variable_type_table_length;
//    {
//      u2 start_pc;
//      u2 length;
//      u2 name_index;
//      u2 signature_index;
//      u2 index;
//    } local_variable_type_table[local_variable_type_table_length];
//  }
// J5TODO: Needs some testing !
public class LocalVariableTypeTable extends Attribute {
    private LocalVariable[] local_variable_type_table;        // variables

    public LocalVariableTypeTable(final int name_index, final int length, final LocalVariable[] local_variable_table, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_LOCAL_VARIABLE_TYPE_TABLE, name_index, length, constant_pool);
        this.local_variable_type_table = local_variable_table;
    }

    LocalVariableTypeTable(final int nameIdx, final int len, final DataInput input, final ConstantPool cpool) throws IOException {
        this(nameIdx, len, (LocalVariable[]) null, cpool);

        final int local_variable_type_table_length = input.readUnsignedShort();
        local_variable_type_table = new LocalVariable[local_variable_type_table_length];

        for (int i = 0; i < local_variable_type_table_length; i++) {
            local_variable_type_table[i] = new LocalVariable(input, cpool);
        }
    }

    public final LocalVariable[] getLocalVariableTypeTable() {
        return local_variable_type_table;
    }

    @Override
    public void accept(final Visitor v) {
        v.visitLocalVariableTypeTable(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder();

        for (int i = 0; i < local_variable_type_table.length; i++) {
            buf.append(local_variable_type_table[i].toStringShared(true));

            if (i < local_variable_type_table.length - 1) {
                buf.append('\n');
            }
        }

        return buf.toString();
    }
}
