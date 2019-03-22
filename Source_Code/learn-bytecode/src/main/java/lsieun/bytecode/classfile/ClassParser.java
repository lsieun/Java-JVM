package lsieun.bytecode.classfile;

import lsieun.bytecode.classfile.basic.JVMConst;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class ClassParser {
    private ByteDashboard byteDashboard;

    public ClassParser(ByteDashboard byteDashboard) {
        this.byteDashboard = byteDashboard;
    }

    public JavaClass parse() {
        // Check magic tag of class file
        MagicNumber magicNumber = readMagicNumber();
        // Get compiler version
        MinorVersion minorVersion = readMinorVersion();
        MajorVersion majorVersion = readMajorVersion();
        // Read constant pool entries
        ConstantPoolCount constantPoolCount = readConstantPoolCount();

        JavaClass javaClass = new JavaClass();
        javaClass.setMagicNumber(magicNumber);
        javaClass.setMinorVersion(minorVersion);
        javaClass.setMajorVersion(majorVersion);
        javaClass.setConstantPoolCount(constantPoolCount);

        return javaClass;
    }

    /**
     * Check whether the header of the file is ok.
     * Of course, this has to be the first action on successive file reads.
     * @throws  ClassFormatException
     */
    private MagicNumber readMagicNumber() {
        byte[] bytes = this.byteDashboard.nextN(4);

        int magic = ByteUtils.toInt(bytes, 0);
        if (magic != JVMConst.JVM_CLASSFILE_MAGIC) {
            throw new ClassFormatException(this.byteDashboard + " is not a Java .class file");
        }

        MagicNumber instance = new MagicNumber();
        instance.setBytes(bytes);
        return instance;
    }

    private MinorVersion readMinorVersion() {
        byte[] bytes = this.byteDashboard.nextN(2);

        MinorVersion instance = new MinorVersion();
        instance.setBytes(bytes);
        return instance;
    }

    private MajorVersion  readMajorVersion() {
        byte[] bytes = this.byteDashboard.nextN(2);

        MajorVersion instance = new MajorVersion();
        instance.setBytes(bytes);
        return instance;
    }

    private ConstantPoolCount readConstantPoolCount() {
        byte[] bytes = this.byteDashboard.nextN(2);

        ConstantPoolCount instance = new ConstantPoolCount();
        instance.setBytes(bytes);
        return instance;
    }


}
