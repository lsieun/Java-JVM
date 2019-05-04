package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class Signature extends AttributeInfo {
    private final int signature_index;
    private final String value;

    public Signature(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] signature_index_bytes = byteDashboard.nextN(2);
        this.signature_index = ByteUtils.bytesToInt(signature_index_bytes, 0);
        this.value = constantPool.getConstantString(signature_index, CPConst.CONSTANT_Utf8);
    }

    public int getSignatureIndex() {
        return signature_index;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitSignature(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("Value='" + this.getValue() + "'");
        list.add("SignatureIndex='" + this.signature_index + "'");
        list.add("HexCode='" + super.getHexCode() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
