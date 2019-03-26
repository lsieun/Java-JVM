package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class SuperClass extends Node {
    private final int super_class_index;
    private String value;

    public SuperClass(byte[] bytes) {
        super.setBytes(bytes);
        this.super_class_index = ByteUtils.bytesToInt(bytes, 0);
        this.value = "#" + super_class_index;
    }

    public int getSuper_class_index() {
        return super_class_index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitSuperClass(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Value='" + this.getValue() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("SuperClass {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
