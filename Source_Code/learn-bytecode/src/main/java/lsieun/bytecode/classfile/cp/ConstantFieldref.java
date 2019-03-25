package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantFieldref extends Constant {
    private final int class_index;
    private final int name_and_type_index;

    private String value;

    ConstantFieldref(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Fieldref);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] class_index_bytes = byteDashboard.nextN(2);
        byte[] name_and_type_index_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, class_index_bytes, name_and_type_index_bytes);

        this.class_index = ByteUtils.bytesToInt(class_index_bytes, 0);
        this.name_and_type_index = ByteUtils.bytesToInt(name_and_type_index_bytes, 0);
        super.setBytes(bytes);
    }

    public int getClassIndex() {
        return class_index;
    }

    public int getNameAndTypeIndex() {
        return name_and_type_index;
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
        obj.visitConstantFieldref(this);
    }
}
