package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.utils.ByteDashboard;
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

    @Override
    public void accept(Visitor obj) {
        obj.visitLineNumberTable(this);
    }
}
