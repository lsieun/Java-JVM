package lsieun.bytecode.classfile;

import lsieun.bytecode.utils.ByteDashboard;

public final class Fields extends Node {
    private final FieldInfo[] entries;

    public Fields(ByteDashboard byteDashboard, int count, ConstantPool constantPool) {
        this.entries = new FieldInfo[count];
        for(int i=0; i<count; i++) {
            FieldInfo fieldInfo = new FieldInfo(byteDashboard, constantPool);
            this.entries[i] = fieldInfo;
        }
    }

    public FieldInfo[] getEntries() {
        return entries;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitFields(this);
    }
}
