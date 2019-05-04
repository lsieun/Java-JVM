package lsieun.bytecode.classfile;

import lsieun.bytecode.classfile.basic.AccessConst;
import lsieun.utils.radix.ByteUtils;

public final class AccessFlags extends Node {
    private final int access_flags;
    private final String value;

    public AccessFlags(byte[] bytes) {
        super.setBytes(bytes);
        this.access_flags = ByteUtils.bytesToInt(bytes, 0);
        this.value = AccessConst.getClassAccessFlagsString(access_flags);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitAccessFlags(this);
    }
}
