package lsieun.bytecode.classfile.attrs.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class LocalVariableTypeTable extends AttributeInfo {
    private final int local_variable_type_table_length;
    private final List<LocalVariableType> local_variable_type_list;

    public LocalVariableTypeTable(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] local_variable_type_table_length_bytes = byteDashboard.nextN(2);
        this.local_variable_type_table_length = ByteUtils.bytesToInt(local_variable_type_table_length_bytes, 0);

        this.local_variable_type_list = new ArrayList();
        for(int i=0; i<local_variable_type_table_length; i++) {
            LocalVariableType item = new LocalVariableType(byteDashboard, constantPool);
            this.local_variable_type_list.add(item);
        }
    }

    public int getLocalVariableTypeTableLength() {
        return local_variable_type_table_length;
    }

    public List<LocalVariableType> getLocalVariableTypeList() {
        return local_variable_type_list;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitLocalVariableTypeTable(this);
    }

    public List<String> getLines() {
        List<String> list = new ArrayList();

        Collections.sort(this.local_variable_type_list);
        if(this.local_variable_type_table_length > 0) {
            list.add("LocalVariableTypeTable:");
            list.add("index  start_pc  length  name_and_type");
        }
        for(int i=0; i<this.local_variable_type_table_length; i++) {
            LocalVariableType item = this.local_variable_type_list.get(i);
            list.add(String.format("%5d  %8d  %6d  ",
                    item.getIndex(), item.getStartPC(), item.getLength())
                    + item.getNameAndType()
            );
        }

        return list;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("AttributeNameIndex='" + this.getAttributeNameIndex() + "'");
        list.add("HexCode='" + super.getHexCode() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
