package lsieun.bytecode.classfile;

import lsieun.utils.radix.ByteUtils;

public final class AttributesCount extends Node {
    private final int value;

    public AttributesCount(byte[] bytes) {
        super.setBytes(bytes);
        this.value = ByteUtils.bytesToInt(bytes, 0);
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitAttributesCount(this);
    }
}
