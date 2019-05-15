package lsieun.bytecode.classfile;

import lsieun.bytecode.utils.ByteDashboard;

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

    @Override
    public void accept(Visitor obj) {
        obj.visitAttributes(this);
    }
}
