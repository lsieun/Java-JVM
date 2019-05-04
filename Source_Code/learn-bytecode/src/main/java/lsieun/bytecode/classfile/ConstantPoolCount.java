package lsieun.bytecode.classfile;

import lsieun.utils.radix.ByteUtils;

public class ConstantPoolCount extends Node {
    private final int value;

    public ConstantPoolCount(byte[] bytes) {
        super.setBytes(bytes);
        this.value = ByteUtils.bytesToInt(bytes, 0);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantPoolCount(this);
    }
}
