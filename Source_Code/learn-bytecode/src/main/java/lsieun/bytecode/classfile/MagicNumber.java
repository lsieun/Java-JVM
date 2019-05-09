package lsieun.bytecode.classfile;

import lsieun.bytecode.generic.cnst.JVMConst;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.utils.radix.ByteUtils;

public class MagicNumber extends Node {

    public MagicNumber(byte[] bytes) {
        super.setBytes(bytes);

        int magic = ByteUtils.bytesToInt(bytes, 0);
        if (magic != JVMConst.JVM_CLASSFILE_MAGIC) {
            throw new ClassFormatException("It is not a Java .class file");
        }
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitMagicNumber(this);
    }

}
