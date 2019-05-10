package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantInteger extends Constant {
    private final Integer intValue;

    ConstantInteger(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Integer);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(4);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.intValue = ByteUtils.toInt(value_bytes);
        super.setValue(String.valueOf(this.intValue));
        super.setBytes(bytes);
    }

    public Integer getIntValue() {
        return intValue;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantInteger(this);
    }
}
