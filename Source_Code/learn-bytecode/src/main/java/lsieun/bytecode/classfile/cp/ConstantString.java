package lsieun.bytecode.classfile.cp;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantString extends Constant {
    private final int string_index;

    ConstantString(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_String);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] value_bytes = byteDashboard.nextN(2);
        byte[] bytes = ByteUtils.merge(tag_bytes, value_bytes);

        this.string_index = ByteUtils.bytesToInt(value_bytes, 0);
        super.setValue("#" + string_index);
        super.setBytes(bytes);
    }

    /**
     * @param string_index Index of Constant_Utf8 in constant pool
     */
    public ConstantString(final int string_index) {
        super(CPConst.CONSTANT_String);
        this.string_index = string_index;
    }

    public int getStringIndex() {
        return string_index;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantString(this);
    }
}
