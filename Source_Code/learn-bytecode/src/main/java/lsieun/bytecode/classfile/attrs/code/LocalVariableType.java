package lsieun.bytecode.classfile.attrs.code;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class LocalVariableType implements Comparable<LocalVariableType> {
    private final int start_pc;
    private final int length;
    private final int name_index;
    private final int signature_index;
    private final int index;
    private final String nameAndType;

    public LocalVariableType(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] start_pc_bytes = byteDashboard.nextN(2);
        byte[] length_bytes = byteDashboard.nextN(2);
        byte[] name_index_bytes = byteDashboard.nextN(2);
        byte[] signature_index_bytes = byteDashboard.nextN(2);
        byte[] index_bytes = byteDashboard.nextN(2);

        this.start_pc = ByteUtils.bytesToInt(start_pc_bytes, 0);
        this.length = ByteUtils.bytesToInt(length_bytes, 0);
        this.name_index = ByteUtils.bytesToInt(name_index_bytes, 0);
        this.signature_index = ByteUtils.bytesToInt(signature_index_bytes, 0);
        this.index = ByteUtils.bytesToInt(index_bytes, 0);

        String name = constantPool.getConstantString(name_index, CPConst.CONSTANT_Utf8);
        String signature = constantPool.getConstantString(signature_index, CPConst.CONSTANT_Utf8);
        this.nameAndType = name + ":" + signature;
    }


    public int getStartPC() {
        return start_pc;
    }

    public int getLength() {
        return length;
    }

    public int getNameIndex() {
        return name_index;
    }

    public int getSignatureIndex() {
        return signature_index;
    }

    public int getIndex() {
        return index;
    }

    public String getNameAndType() {
        return nameAndType;
    }

    @Override
    public int compareTo(LocalVariableType another) {
        int thisIndex = this.index;
        int anotherIndex = another.getIndex();
        return (thisIndex - anotherIndex);
    }
}
