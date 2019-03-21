package lsieun.bytecode.classfile;

import lsieun.utils.StringUtils;

public class JavaClass {
    private MagicNumber magicNumber;

    public MagicNumber getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(MagicNumber magicNumber) {
        this.magicNumber = magicNumber;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("ClassFile:" + StringUtils.LF);
        buff.append(magicNumber + StringUtils.LF);
        return buff.toString();
    }
}
