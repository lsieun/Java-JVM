package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public final class Fields extends Node {
    private final List<FieldInfo> field_list;

    public Fields(ByteDashboard byteDashboard, int count, ConstantPool constantPool) {
        this.field_list = new ArrayList();
        for(int i=0; i<count; i++) {
            FieldInfo fieldInfo = new FieldInfo(byteDashboard, constantPool);
            this.field_list.add(fieldInfo);
        }
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitFields(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();

        for(int i=0; i<this.field_list.size(); i++) {
            FieldInfo item = this.field_list.get(i);
            list.add("    " + item + StringUtils.LF);
        }

        String content = StringUtils.list2str(list, "");

        StringBuilder buf = new StringBuilder();
        buf.append("Fields {" + StringUtils.LF);
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
