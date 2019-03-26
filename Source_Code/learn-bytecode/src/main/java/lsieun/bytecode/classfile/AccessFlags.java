package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.basic.AccessConst;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class AccessFlags extends Node {
    private final int access_flags;
    private final String value;

    public AccessFlags(byte[] bytes) {
        super.setBytes(bytes);
        this.access_flags = ByteUtils.bytesToInt(bytes, 0);
        this.value = AccessConst.getClassAccessFlagsString(access_flags);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitAccessFlags(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Value='" + this.getValue() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("AccessFlags {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
