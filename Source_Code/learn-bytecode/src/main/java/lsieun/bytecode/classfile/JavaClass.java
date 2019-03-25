package lsieun.bytecode.classfile;

import lsieun.utils.StringUtils;

public class JavaClass {
    private MagicNumber magicNumber;
    private MinorVersion minorVersion;
    private MajorVersion majorVersion;
    private ConstantPoolCount constantPoolCount;
    private ConstantPool constantPool;

    // region getters and setters
    public MagicNumber getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(MagicNumber magicNumber) {
        this.magicNumber = magicNumber;
    }

    public MinorVersion getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(MinorVersion minorVersion) {
        this.minorVersion = minorVersion;
    }

    public MajorVersion getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(MajorVersion majorVersion) {
        this.majorVersion = majorVersion;
    }

    public ConstantPoolCount getConstantPoolCount() {
        return constantPoolCount;
    }

    public void setConstantPoolCount(ConstantPoolCount constantPoolCount) {
        this.constantPoolCount = constantPoolCount;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }
// endregion

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("ClassFile:" + StringUtils.LF);
        buff.append(magicNumber + StringUtils.LF);
        buff.append(minorVersion + StringUtils.LF);
        buff.append(majorVersion + StringUtils.LF);
        buff.append(constantPoolCount + StringUtils.LF);
        buff.append(constantPool + StringUtils.LF);
        return buff.toString();
    }
}
