package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class Interfaces extends Node {
    private final List<Integer> interface_index_list;
    private String value;

    public Interfaces(ByteDashboard byteDashboard, int count) {
        byte[] bytes = byteDashboard.peekN(count * 2);
        super.setBytes(bytes);

        this.interface_index_list = new ArrayList();
        List<String> index_list = new ArrayList();

        for(int i=0; i<count; i++) {
            byte[] interface_index_bytes = byteDashboard.nextN(2);
            int interface_index = ByteUtils.bytesToInt(interface_index_bytes, 0);
            this.interface_index_list.add(interface_index);
            index_list.add("#" + interface_index);
        }
        this.value = StringUtils.list2str(index_list, "[", "]", ",");
    }

    public List<Integer> getInterfaceIndexList() {
        return interface_index_list;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitInterfaces(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("HexCode='" + super.getHexCode() + "'");
        list.add("Value='" + this.getValue() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append("Interfaces {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
