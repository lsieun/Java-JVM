package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class SourceFile extends AttributeInfo {
    private final int sourcefile_index;
    private final String value;

    public SourceFile(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool);
        byte[] sourcefile_index_bytes = super.getInfo();
        this.sourcefile_index = ByteUtils.bytesToInt(sourcefile_index_bytes, 0);

        String value = constantPool.getConstantString(sourcefile_index, CPConst.CONSTANT_Utf8);
        this.value = value;
    }

    public int getSourcefileIndex() {
        return sourcefile_index;
    }

    public String getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("Value='" + this.getValue() + "'");
        list.add("HexCode='" + super.getHexCode() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
