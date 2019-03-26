package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class InterfacesCount extends Node {
    private final int value;

    public InterfacesCount(byte[] bytes) {
        super.setBytes(bytes);
        this.value = ByteUtils.bytesToInt(bytes, 0);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitInterfacesCount(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Value='" + this.getValue() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("InterfacesCount {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
