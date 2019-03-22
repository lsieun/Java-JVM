package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;

public class ConstantPool extends Node {
    @Override
    public void accept(Visitor obj) {
        obj.visitConstantPool(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");


        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("ConstantPoolCount {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
