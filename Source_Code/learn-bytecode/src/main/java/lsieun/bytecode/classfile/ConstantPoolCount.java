package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public class ConstantPoolCount extends Node {
    private final int value;

    public ConstantPoolCount(byte[] bytes) {
        super.setBytes(bytes);
        this.value = ByteUtils.bytesToInt(bytes, 0);
    }

    public int getValue() {
        return this.value;
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
