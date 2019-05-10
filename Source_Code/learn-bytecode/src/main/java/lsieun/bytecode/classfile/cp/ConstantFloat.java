package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantFloat extends Constant {
    private final Float floatValue;

    ConstantFloat(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Float);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(4);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.floatValue = ByteUtils.toFloat(value_bytes);
        super.setValue(String.valueOf(this.floatValue));
        super.setBytes(bytes);
    }

    public Float getFloatValue() {
        return floatValue;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantFloat(this);
    }
}
