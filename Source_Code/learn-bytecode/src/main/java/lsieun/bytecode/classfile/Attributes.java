package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public final class Attributes extends Node {
    private final AttributeInfo[] entries;

    public Attributes(ByteDashboard byteDashboard, int count, ConstantPool constantPool) {
        this.entries = new AttributeInfo[count];
        for(int i=0; i<count; i++) {
            AttributeInfo attr = AttributeInfo.read(byteDashboard, constantPool);
            this.entries[i] = attr;
        }
    }

    public AttributeInfo[] getEntries() {
        return entries;
    }

    public AttributeInfo findAttribute(String attrName) {
        if(StringUtils.isBlank(attrName)) return null;
        for(int i=0; i<entries.length; i++) {
            AttributeInfo item = entries[i];
            String name = item.getName();
            if(attrName.equals(name)) {
                return item;
            }
        }
        return null;
    }

    public String getAttributesName() {
        List<String> attr_list = new ArrayList();
        for(int i=0; i<entries.length; i++) {
            AttributeInfo item = entries[i];
            String name = item.getName();
            attr_list.add(name);
        }

        String attrNames = StringUtils.list2str(attr_list, "[", "]", ",");
        if(attrNames == null) {
            attrNames = "[]";
        }
        return attrNames;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitAttributes(this);
    }
}
