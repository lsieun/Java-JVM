package lsieun.bytecode.classfile.attrs.classfile;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
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
    public void accept(Visitor obj) {
        obj.visitSourceFile(this);
    }
}
