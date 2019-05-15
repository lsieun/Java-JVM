package lsieun.bytecode.classfile;

import lsieun.bytecode.utils.ByteDashboard;

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

    @Override
    public void accept(Visitor obj) {
        obj.visitMethods(this);
    }
}
