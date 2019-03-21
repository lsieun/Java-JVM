package lsieun.bytecode.classfile;

public class MagicNumber extends Node {

    @Override
    public void accept(Visitor obj) {
        obj.visitMagicNumber(this);
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("MagicNumber {");
        buff.append("HexCode='" + super.getHexCode() + "'");
        buff.append("}");
        return buff.toString();
    }
}
