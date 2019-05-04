package lsieun.bytecode.classfile;

import lsieun.utils.radix.ByteUtils;

public class MajorVersion extends Node {
    public MajorVersion(byte[] bytes) {
        super.setBytes(bytes);
    }

    public String getJDKVersion() {
        int num = ByteUtils.bytesToInt(super.getBytes(), 45);
        return "JDK "  + (num - 44);
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitMajorVersion(this);
    }
}
