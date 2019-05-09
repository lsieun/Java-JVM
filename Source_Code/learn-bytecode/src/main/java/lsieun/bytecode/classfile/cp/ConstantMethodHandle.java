package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantMethodHandle extends Constant {
    private final int reference_kind;
    private final int reference_index;

    ConstantMethodHandle(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_MethodHandle);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] reference_kind_bytes = byteDashboard.nextN(1);
        byte[] reference_index_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, reference_kind_bytes, reference_index_bytes);

        this.reference_kind = ByteUtils.bytesToInt(reference_kind_bytes, 0);
        this.reference_index = ByteUtils.bytesToInt(reference_index_bytes, 0);
        super.setValue("#" + reference_index);
        super.setBytes(bytes);
    }

    public int getReferenceKind() {
        return reference_kind;
    }

    public int getReferenceIndex() {
        return reference_index;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantMethodHandle(this);
    }
}
