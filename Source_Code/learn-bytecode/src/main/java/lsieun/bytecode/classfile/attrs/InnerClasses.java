package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class InnerClasses extends AttributeInfo {
    private final int number_of_classes;
    private List<InnerClass> inner_class_list;

    public InnerClasses(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] number_of_classes_bytes = byteDashboard.nextN(2);
        this.number_of_classes = ByteUtils.bytesToInt(number_of_classes_bytes, 0);

        this.inner_class_list = new ArrayList();
        for(int i=0; i<number_of_classes; i++) {
            InnerClass item = new InnerClass(byteDashboard, constantPool);
            this.inner_class_list.add(item);
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        //list.add("Value='" + this.getValue() + "'");

        List<String> innerClassList = new ArrayList();
        for(int i=0; i<this.inner_class_list.size(); i++) {
            InnerClass item = this.inner_class_list.get(i);
            innerClassList.add(item.getName());
        }
        list.add("Value='" + StringUtils.list2str(innerClassList, "[","]",", ") + "'");
        list.add("HexCode='" + super.getHexCode() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
