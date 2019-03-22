package lsieun.bytecode.classfile;

import lsieun.bytecode.classfile.cp.ConstantUtf8;

public interface Visitor {
    void visitMagicNumber(MagicNumber obj);

    void visitMinorVersion(MinorVersion obj);

    void visitMajorVersion(MajorVersion obj);

    void visitConstantPoolCount(ConstantPoolCount obj);

    void visitConstantPool(ConstantPool obj);

    // region constant pool
    void visitConstantUtf8(ConstantUtf8 obj);
    // endregion
}
