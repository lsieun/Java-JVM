package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class AttributesCount extends Node {
    private final int value;

    public AttributesCount(byte[] bytes) {
        super.setBytes(bytes);
        this.value = ByteUtils.bytesToInt(bytes, 0);
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitAttributesCount(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Value='" + this.getValue() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("AttributesCount {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
