package lsieun.bytecode.classfile.cp;

import java.nio.charset.StandardCharsets;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantUtf8 extends Constant {
    private final int length;
    private final String value;


    ConstantUtf8(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Utf8);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] length_bytes = byteDashboard.nextN(2);
        int length = ByteUtils.bytesToInt(length_bytes, 0);
        byte[] utf_bytes = byteDashboard.nextN(length);
        byte[] bytes = ByteUtils.merge(tag_bytes, length_bytes, utf_bytes);

        this.length = length;
        this.value = new String(utf_bytes, StandardCharsets.UTF_8);
        super.setBytes(bytes);
    }

    public int getLength() {
        return length;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        // do nothing
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantUtf8(this);
    }
}
