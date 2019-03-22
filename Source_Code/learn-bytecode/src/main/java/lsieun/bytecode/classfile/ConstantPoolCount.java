package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public class ConstantPoolCount extends Node {
    public int getValue() {
        int num = ByteUtils.toInt(super.getBytes(), 0);
        return num;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantPoolCount(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Value='" + this.getValue() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("ConstantPoolCount {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
