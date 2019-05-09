package lsieun.bytecode.classfile.cp;

import java.nio.charset.StandardCharsets;

import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ConstantUtf8 extends Constant {
    private final int length;
    private final String utf8Value;


    ConstantUtf8(ByteDashboard byteDashboard) {
        super(CPConst.CONSTANT_Utf8);
        byte[] tag_bytes = byteDashboard.nextN(1);
        byte[] length_bytes = byteDashboard.nextN(2);
        int length = ByteUtils.bytesToInt(length_bytes, 0);
        byte[] utf_bytes = byteDashboard.nextN(length);
        byte[] bytes = ByteUtils.merge(tag_bytes, length_bytes, utf_bytes);

        this.length = length;
        this.utf8Value = new String(utf_bytes, StandardCharsets.UTF_8);
        super.setValue(utf8Value);
        super.setBytes(bytes);
    }

    public int getLength() {
        return length;
    }

    public String getUtf8Value() {
        return utf8Value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantUtf8(this);
    }
}
