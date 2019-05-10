package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantPackage extends Constant {
    private final int name_index;

    ConstantPackage(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Package);
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
        obj.visitConstantPackage(this);
    }
}
