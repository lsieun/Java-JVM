package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class LocalVariableTable extends AttributeInfo {
    private final int local_variable_table_length;
    private final List<LocalVariable> local_variable_list;

    public LocalVariableTable(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] local_variable_table_length_bytes = byteDashboard.nextN(2);
        this.local_variable_table_length = ByteUtils.bytesToInt(local_variable_table_length_bytes, 0);

        this.local_variable_list = new ArrayList();
        for(int i=0; i<local_variable_table_length; i++) {
            LocalVariable item = new LocalVariable(byteDashboard, constantPool);
            this.local_variable_list.add(item);
        }
    }

    public int getLocalVariableTableLength() {
        return local_variable_table_length;
    }

    public List<LocalVariable> getLocalVariableList() {
        return local_variable_list;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitLocalVariableTable(this);
    }
}
