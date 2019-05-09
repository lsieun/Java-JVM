package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantLong extends Constant {
    private final Long longValue;

    ConstantLong(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Long);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(8);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.longValue = ByteUtils.toLong(value_bytes);
        super.setValue(String.valueOf(this.longValue));
        super.setBytes(bytes);
    }

    public Long getLongValue() {
        return longValue;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantLong(this);
    }
}
