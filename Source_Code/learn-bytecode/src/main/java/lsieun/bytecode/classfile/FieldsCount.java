package lsieun.bytecode.classfile;

import lsieun.utils.radix.ByteUtils;

public final class FieldsCount extends Node {
    private final int value;

    public FieldsCount(byte[] bytes) {
        super.setBytes(bytes);
        this.value = ByteUtils.bytesToInt(bytes, 0);
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitFieldsCount(this);
    }
}
