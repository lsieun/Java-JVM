package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantInterfaceMethodref extends ConstantRef {

    public ConstantInterfaceMethodref(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_InterfaceMethodref);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] class_index_bytes = byteDashboard.nextN(2);
        byte[] name_and_type_index_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, class_index_bytes, name_and_type_index_bytes);

        int class_index = ByteUtils.bytesToInt(class_index_bytes, 0);
        int name_and_type_index = ByteUtils.bytesToInt(name_and_type_index_bytes, 0);

        super.setClassIndex(class_index);
        super.setNameAndTypeIndex(name_and_type_index);
        super.setValue("#" + class_index + ".#" + name_and_type_index);
        super.setBytes(bytes);
    }

    /**
     * @param class_index Reference to the class containing the method
     * @param name_and_type_index and the method signature
     */
    public ConstantInterfaceMethodref(final int class_index, final int name_and_type_index) {
        super(CPConst.CONSTANT_InterfaceMethodref);
        super.setClassIndex(class_index);
        super.setNameAndTypeIndex(name_and_type_index);
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantInterfaceMethodref(this);
    }
}
