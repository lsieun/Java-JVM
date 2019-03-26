package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public class MajorVersion extends Node {
    public MajorVersion(byte[] bytes) {
        super.setBytes(bytes);
    }

    public String getJDKVersion() {
        int num = ByteUtils.bytesToInt(super.getBytes(), 45);
        return "JDK "  + (num - 44);
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitMajorVersion(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Compiler-Version='" + this.getJDKVersion() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("MajorVersion {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
