package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public final class Methods extends Node {
    private final MethodInfo[] entries;

    public Methods(ByteDashboard byteDashboard, int count, ConstantPool constantPool) {
        this.entries = new MethodInfo[count];
        for(int i=0; i<count; i++) {
            MethodInfo methodInfo = new MethodInfo(byteDashboard, constantPool);
            this.entries[i] = methodInfo;
        }
    }

    public MethodInfo[] getEntries() {
        return entries;
    }

    public String getMethodNames() {
        List<String> list = new ArrayList();
        for(MethodInfo item : entries) {
            String value = item.getValue();
            list.add(value);
        }
        return StringUtils.list2str(list, ", ");
    }

    public MethodInfo findByNameAndType(String nameAndType) {
        if(StringUtils.isBlank(nameAndType)) return null;

        for(int i = 0; i<this.entries.length; i++) {
            MethodInfo item = this.entries[i];
            String value = item.getValue();
            if(nameAndType.equals(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitMethods(this);
    }
}
