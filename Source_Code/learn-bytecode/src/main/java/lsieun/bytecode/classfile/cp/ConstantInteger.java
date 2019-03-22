package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantInteger extends Constant {
    private final Integer value;

    ConstantInteger(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Integer);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(4);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.value = ByteUtils.toInt(value_bytes, 0);
        super.setBytes(bytes);
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void accept(Visitor obj) {

    }
}
