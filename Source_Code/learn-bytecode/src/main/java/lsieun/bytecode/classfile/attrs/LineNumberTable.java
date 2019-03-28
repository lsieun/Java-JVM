package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class LineNumberTable extends AttributeInfo {
    private final int line_number_table_length;
    private final List<LineNumber> line_number_list;

    public LineNumberTable(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] line_number_table_length_bytes = byteDashboard.nextN(2);
        this.line_number_table_length = ByteUtils.bytesToInt(line_number_table_length_bytes, 0);

        this.line_number_list = new ArrayList();
        for(int i=0; i<line_number_table_length; i++) {
            LineNumber item = new LineNumber(byteDashboard);
            this.line_number_list.add(item);
        }
    }

    public int getLineNumberTableLength() {
        return line_number_table_length;
    }

    public List<LineNumber> getLineNumberList() {
        return line_number_list;
    }

    public List<String> getLines() {
        List<String> list = new ArrayList();

        if(this.line_number_list.size() > 0) {
            list.add("LineNumberTable:");
            list.add("start_pc  line_number");
        }
        for(int i=0; i<this.line_number_list.size(); i++) {
            LineNumber item = this.line_number_list.get(i);
            list.add(String.format("%8d  %11d", item.getStartPC(), item.getLineNumber()));
        }

        return list;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("AttributeNameIndex='" + this.getAttributeNameIndex() + "'");
        list.add("HexCode='" + super.getHexCode() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
