package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantDouble extends Constant {
    private final Double value;

    ConstantDouble(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Double);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(8);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.value = ByteUtils.toDouble(value_bytes);
        super.setBytes(bytes);
    }

    @Override
    public String getValue() {
        return String.valueOf(this.value);
    }

    @Override
    public void setValue(String value) {
        // do nothing
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantDouble(this);
    }
}
