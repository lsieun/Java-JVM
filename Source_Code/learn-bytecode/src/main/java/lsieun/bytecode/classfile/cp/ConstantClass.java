package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantClass extends Constant {
    private final int name_index;

    ConstantClass(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Class);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.name_index = ByteUtils.bytesToInt(value_bytes, 0);
        super.setValue("#" + this.name_index);
        super.setBytes(bytes);
    }

    public int getNameIndex() {
        return name_index;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantClass(this);
    }
}
