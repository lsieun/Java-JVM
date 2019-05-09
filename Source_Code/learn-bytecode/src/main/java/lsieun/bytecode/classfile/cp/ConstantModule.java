package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantModule extends Constant {
    private final int name_index;

    ConstantModule(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Module);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] name_index_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, name_index_bytes);

        this.name_index = ByteUtils.bytesToInt(name_index_bytes, 0);
        super.setBytes(bytes);
    }

    public int getNameIndex() {
        return name_index;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantModule(this);
    }
}
