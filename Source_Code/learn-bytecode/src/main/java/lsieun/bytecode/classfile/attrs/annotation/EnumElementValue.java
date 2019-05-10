package lsieun.bytecode.classfile.attrs.annotation;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class EnumElementValue extends ElementValue {
    private final int type_name_index;
    private final int const_name_index;
    private final String value;

    public EnumElementValue(ByteDashboard byteDashboard, final ConstantPool constantPool) {
        super(byteDashboard);

        byte[] type_name_index_bytes = byteDashboard.nextN(2);
        byte[] const_name_index_bytes = byteDashboard.nextN(2);
        this.type_name_index = ByteUtils.bytesToInt(type_name_index_bytes, 0);
        this.const_name_index = ByteUtils.bytesToInt(const_name_index_bytes, 0);

        String type_name = constantPool.getConstantString(type_name_index, CPConst.CONSTANT_Utf8);
        String const_name = constantPool.getConstantString(const_name_index, CPConst.CONSTANT_Utf8);
        // FIXME：验证这里的是否准确
        this.value = const_name + ":" + type_name;
    }

    @Override
    public String stringifyValue() {
        return this.value;
    }
}
