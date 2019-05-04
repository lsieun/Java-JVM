package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public final class Deprecated extends AttributeInfo {
    public Deprecated(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool);
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitDeprecated(this);
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
