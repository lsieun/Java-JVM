package lsieun.bytecode.classfile.attrs.code;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class LocalVariable implements Comparable<LocalVariable> {
    private final int start_pc; // Range in which the variable is valid
    private final int length;
    private final int name_index; // Index in constant pool of variable name
    private final int descriptor_index; // Index of variable signature
    private final int index; /* Variable is `index'th local variable on
     * this method's frame.
     */
    private final String nameAndType;

    private ConstantPool constant_pool;
    private int orig_index;

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

        this.constant_pool = constantPool;
    }

    /**
     * @param start_pc Range in which the variable
     * @param length ... is valid
     * @param name_index Index in constant pool of variable name
     * @param signature_index Index of variable's signature
     * @param index Variable is `index'th local variable on the method's frame
     * @param constant_pool Array of constants
     * @param orig_index Variable is `index'th local variable on the method's frame prior to any changes
     */
    public LocalVariable(final int start_pc, final int length, final int name_index, final int signature_index, final int index,
                         final ConstantPool constant_pool, final int orig_index) {
        this.start_pc = start_pc;
        this.length = length;
        this.name_index = name_index;
        this.descriptor_index = signature_index;
        this.index = index;
        this.constant_pool = constant_pool;
        this.orig_index = orig_index;

        String name = constant_pool.getConstantString(name_index, CPConst.CONSTANT_Utf8);
        String descriptor = constant_pool.getConstantString(signature_index, CPConst.CONSTANT_Utf8);
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
