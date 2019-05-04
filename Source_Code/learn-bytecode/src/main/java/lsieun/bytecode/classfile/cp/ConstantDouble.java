package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantDouble extends Constant {
    private final Double doubleValue;

    ConstantDouble(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Double);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(8);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.doubleValue = ByteUtils.toDouble(value_bytes);
        super.setValue(String.valueOf(this.doubleValue));
        super.setBytes(bytes);
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantDouble(this);
    }
}
