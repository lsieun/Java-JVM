package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class ThisClass extends Node {
    private final int this_class_index;
    private String value;

    public ThisClass(byte[] bytes) {
        super.setBytes(bytes);
        this.this_class_index = ByteUtils.bytesToInt(bytes, 0);
        this.value = "#" + this_class_index;
    }

    public int getThis_class_index() {
        return this_class_index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitThisClass(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Value='" + this.getValue() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("ThisClass {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
