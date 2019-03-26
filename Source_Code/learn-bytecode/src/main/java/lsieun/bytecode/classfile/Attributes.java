package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public final class Attributes extends Node {
    private final List<AttributeInfo> attribute_list;

    public Attributes(ByteDashboard byteDashboard, int count, ConstantPool constantPool) {
        this.attribute_list = new ArrayList();
        for(int i=0; i<count; i++) {
            AttributeInfo attr = AttributeInfo.read(byteDashboard, constantPool);
            this.attribute_list.add(attr);
        }
    }

    public List<AttributeInfo> getAttributeList() {
        return attribute_list;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitAttributes(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();

        for(int i=0; i<this.attribute_list.size(); i++) {
            AttributeInfo item = this.attribute_list.get(i);
            list.add("    " + item + StringUtils.LF);
        }

        String content = StringUtils.list2str(list, "");

        StringBuilder buf = new StringBuilder();
        buf.append("Attributes {" + StringUtils.LF);
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
