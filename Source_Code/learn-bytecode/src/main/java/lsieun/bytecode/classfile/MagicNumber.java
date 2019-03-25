package lsieun.bytecode.classfile;

public class MagicNumber extends Node {

    @Override
    public void accept(Visitor obj) {
        obj.visitMagicNumber(this);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("MagicNumber {");
        buf.append("HexCode='" + super.getHexCode() + "'");
        buf.append("}");
        return buf.toString();
    }
}