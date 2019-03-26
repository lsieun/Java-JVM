package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
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

    public List<String> getLines() {
        List<String> list = new ArrayList();

        Collections.sort(this.local_variable_list);
        if(this.local_variable_list.size() > 0) {
            list.add("LocalVariableTable:");
            list.add("index  start_pc  length  name_and_type");
        }
        for(int i=0; i<this.local_variable_list.size(); i++) {
            LocalVariable item = this.local_variable_list.get(i);
            list.add(String.format("%5d  %8d  %6d  ",
                    item.getIndex(), item.getStartPC(), item.getLength())
                    + item.getNameAndType()
            );
        }

        return list;
    }
}
