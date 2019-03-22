package lsieun.bytecode.classfile;

public class MinorVersion extends Node {
    @Override
    public void accept(Visitor obj) {
        obj.visitMinorVersion(this);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("MinorVersion {");
        buf.append("HexCode='" + super.getHexCode() + "'");
        buf.append("}");
        return buf.toString();
    }
}
