package lsieun.bytecode.classfile.attrs.code;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class LocalVariableTable extends AttributeInfo {
    private final int size;
    private final LocalVariable[] entries;

    public LocalVariableTable(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] local_variable_table_length_bytes = byteDashboard.nextN(2);
        this.size = ByteUtils.bytesToInt(local_variable_table_length_bytes, 0);

        this.entries = new LocalVariable[size];
        for(int i = 0; i< size; i++) {
            LocalVariable item = new LocalVariable(byteDashboard, constantPool);
            this.entries[i] = item;
        }
    }

    public int getSize() {
        return size;
    }

    public LocalVariable[] getLocalVariableList() {
        return entries;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitLocalVariableTable(this);
    }
}
