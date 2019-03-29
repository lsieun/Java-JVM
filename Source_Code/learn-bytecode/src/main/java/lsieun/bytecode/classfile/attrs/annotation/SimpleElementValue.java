package lsieun.bytecode.classfile.attrs.annotation;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class SimpleElementValue extends ElementValue {
    private final int const_value_index;
    private final String value;

    public SimpleElementValue(ByteDashboard byteDashboard, final ConstantPool constantPool) {
        super(byteDashboard);

        byte[] const_value_index_bytes = byteDashboard.nextN(2);
        this.const_value_index = ByteUtils.bytesToInt(const_value_index_bytes, 0);

        this.value = constantPool.getConstant(const_value_index).getValue();
    }

    @Override
    public String stringifyValue() {
        return this.value;
    }
}
