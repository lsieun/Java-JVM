package lsieun.bytecode.classfile.attrs.code;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class LocalVariable implements Comparable<LocalVariable> {
    private final int start_pc;
    private final int length;
    private final int name_index;
    private final int descriptor_index;
    private final int index;
    private final String nameAndType;

    public LocalVariable(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] start_pc_bytes = byteDashboard.nextN(2);
        byte[] length_bytes = byteDashboard.nextN(2);
        byte[] name_index_bytes = byteDashboard.nextN(2);
        byte[] descriptor_index_bytes = byteDashboard.nextN(2);
        byte[] index_bytes = byteDashboard.nextN(2);

        this.start_pc = ByteUtils.bytesToInt(start_pc_bytes, 0);
        this.length = ByteUtils.bytesToInt(length_bytes, 0);
        this.name_index = ByteUtils.bytesToInt(name_index_bytes, 0);
        this.descriptor_index = ByteUtils.bytesToInt(descriptor_index_bytes, 0);
        this.index = ByteUtils.bytesToInt(index_bytes, 0);

        String name = constantPool.getConstantString(name_index, CPConst.CONSTANT_Utf8);
        String descriptor = constantPool.getConstantString(descriptor_index, CPConst.CONSTANT_Utf8);
        this.nameAndType = name + ":" + descriptor;
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

    public int getDescriptorIndex() {
        return descriptor_index;
    }

    public int getIndex() {
        return index;
    }

    public String getNameAndType() {
        return nameAndType;
    }

    @Override
    public int compareTo(LocalVariable another) {
        int thisIndex = this.index;
        int anotherIndex = another.getIndex();
        return (thisIndex - anotherIndex);
    }
}
