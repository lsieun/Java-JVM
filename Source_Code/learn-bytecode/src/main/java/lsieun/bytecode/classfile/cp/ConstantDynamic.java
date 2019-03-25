package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantDynamic extends Constant {
    private final int bootstrap_method_attr_index;
    private final int name_and_type_index;

    private String value;

    ConstantDynamic(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Dynamic);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] bootstrap_method_attr_index_bytes = byteDashboard.nextN(2);
        byte[] name_and_type_index_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, bootstrap_method_attr_index_bytes, name_and_type_index_bytes);

        this.bootstrap_method_attr_index = ByteUtils.bytesToInt(bootstrap_method_attr_index_bytes, 0);
        this.name_and_type_index = ByteUtils.bytesToInt(name_and_type_index_bytes, 0);
        super.setBytes(bytes);
    }

    public int getBootstrapMethodAttrIndex() {
        return bootstrap_method_attr_index;
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
        obj.visitConstantDynamic(this);
    }
}