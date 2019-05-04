package lsieun.bytecode.classfile;

import lsieun.utils.radix.ByteUtils;

public final class SuperClass extends Node {
    private final int super_class_index;
    private String value;

    public SuperClass(byte[] bytes) {
        super.setBytes(bytes);
        this.super_class_index = ByteUtils.bytesToInt(bytes, 0);
        this.value = "#" + super_class_index;
    }

    public int getSuper_class_index() {
        return super_class_index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitSuperClass(this);
    }
}
