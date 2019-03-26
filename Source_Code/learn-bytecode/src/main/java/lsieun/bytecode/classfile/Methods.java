package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public final class Methods extends Node {
    private final List<MethodInfo> method_list;

    public Methods(ByteDashboard byteDashboard, int count, ConstantPool constantPool) {
        this.method_list = new ArrayList();
        for(int i=0; i<count; i++) {
            MethodInfo methodInfo = new MethodInfo(byteDashboard, constantPool);
            this.method_list.add(methodInfo);
        }
    }

    public List<MethodInfo> getMethodList() {
        return method_list;
    }

    public MethodInfo findByNameAndType(String nameAndType) {
        if(StringUtils.isBlank(nameAndType)) return null;

        for(int i=0; i<this.method_list.size(); i++) {
            MethodInfo item = this.method_list.get(i);
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

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();

        for(int i = 0; i<this.method_list.size(); i++) {
            MethodInfo item = this.method_list.get(i);
            list.add("    " + item + StringUtils.LF);
        }

        String content = StringUtils.list2str(list, "");

        StringBuilder buf = new StringBuilder();
        buf.append("Methods {" + StringUtils.LF);
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
