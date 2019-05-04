package lsieun.bytecode.classfile;

public class MinorVersion extends Node {
    public MinorVersion(byte[] bytes) {
        super.setBytes(bytes);
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitMinorVersion(this);
    }
}
