package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantMethodType extends Constant {
    private final int descriptor_index;

    private String value;

    ConstantMethodType(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_MethodType);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] descriptor_index_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, descriptor_index_bytes);

        this.descriptor_index = ByteUtils.bytesToInt(descriptor_index_bytes, 0);
        super.setBytes(bytes);
    }

    public int getDescriptorIndex() {
        return descriptor_index;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantMethodType(this);
    }
}
