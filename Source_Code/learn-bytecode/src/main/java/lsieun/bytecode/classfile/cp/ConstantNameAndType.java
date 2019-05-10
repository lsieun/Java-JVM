package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantNameAndType extends Constant {
    private final int name_index;
    private final int descriptor_index;

    ConstantNameAndType(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_NameAndType);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] name_index_bytes = byteDashboard.nextN(2);
        byte[] descriptor_index_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, name_index_bytes, descriptor_index_bytes);

        this.name_index = ByteUtils.bytesToInt(name_index_bytes, 0);
        this.descriptor_index = ByteUtils.bytesToInt(descriptor_index_bytes, 0);
        super.setValue("#" + name_index + ":#" + descriptor_index);
        super.setBytes(bytes);
    }

    public int getNameIndex() {
        return name_index;
    }

    public int getDescriptorIndex() {
        return descriptor_index;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantNameAndType(this);
    }
}
