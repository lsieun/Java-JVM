package lsieun.bytecode.classfile.attrs;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantValue extends AttributeInfo {
    private final int constantvalue_index;
    private final String value;

    public ConstantValue(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] constantvalue_index_bytes = byteDashboard.nextN(2);
        this.constantvalue_index = ByteUtils.bytesToInt(constantvalue_index_bytes, 0);
        this.value = constantPool.getConstant(constantvalue_index).getValue();
    }

    public int getConstantValueIndex() {
        return constantvalue_index;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantValue(this);
    }
}
