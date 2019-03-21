package lsieun.bytecode.utils;

import lsieun.bytecode.classfile.JavaClass;
import lsieun.bytecode.classfile.MagicNumber;
import lsieun.bytecode.classfile.consts.JVMConst;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.utils.radix.HexUtils;

public class ClassParser {
    private ByteDashboard byteDashboard;

    public ClassParser(ByteDashboard byteDashboard) {
        this.byteDashboard = byteDashboard;
    }

    public JavaClass parse() {
        // Check magic tag of class file
        MagicNumber magicNumber = readMagicNumber();

        JavaClass javaClass = new JavaClass();
        javaClass.setMagicNumber(magicNumber);
        return javaClass;
    }

    /**
     * Check whether the header of the file is ok.
     * Of course, this has to be the first action on successive file reads.
     * @throws  ClassFormatException
     */
    private MagicNumber readMagicNumber() {
        byte[] bytes = new byte[4];
        this.byteDashboard.nextN(bytes);

        String hexCode = HexUtils.fromBytes(bytes);
        int magic = HexUtils.toInt(hexCode);

        if (magic != JVMConst.JVM_CLASSFILE_MAGIC) {
            throw new ClassFormatException(this.byteDashboard + " is not a Java .class file");
        }

        MagicNumber instance = new MagicNumber();
        instance.setBytes(bytes);
        return instance;
    }
}
