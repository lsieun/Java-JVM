package lsieun.bytecode.classfile;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

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

    public FieldInfo findByNameAndType(String nameAndType) {
        if(StringUtils.isBlank(nameAndType)) return null;

        for(int i = 0; i<entries.length; i++) {
            FieldInfo item = this.entries[i];
            String value = item.getValue();
            if(nameAndType.equals(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitFields(this);
    }
}
