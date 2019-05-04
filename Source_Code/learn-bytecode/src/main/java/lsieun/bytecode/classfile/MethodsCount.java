package lsieun.bytecode.classfile;

import lsieun.utils.radix.ByteUtils;

public final class MethodsCount extends Node {
    private final int value;

    public MethodsCount(byte[] bytes) {
        super.setBytes(bytes);
        this.value = ByteUtils.bytesToInt(bytes, 0);
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitMethodsCount(this);
    }
}
