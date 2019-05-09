package lsieun.bytecode.classfile.attrs.annotation;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ClassElementValue extends ElementValue {
    private final int class_info_index;
    private final String value;

    public ClassElementValue(ByteDashboard byteDashboard, final ConstantPool constantPool) {
        super(byteDashboard);

        byte[] class_info_index_bytes = byteDashboard.nextN(2);
        this.class_info_index = ByteUtils.bytesToInt(class_info_index_bytes, 0);

        this.value = constantPool.getConstantString(class_info_index, CPConst.CONSTANT_Utf8);
    }

    @Override
    public String stringifyValue() {
        return this.value;
    }
}
