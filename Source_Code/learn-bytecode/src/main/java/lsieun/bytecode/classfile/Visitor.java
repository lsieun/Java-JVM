package lsieun.bytecode.classfile;

import lsieun.bytecode.classfile.cp.ConstantClass;
import lsieun.bytecode.classfile.cp.ConstantDouble;
import lsieun.bytecode.classfile.cp.ConstantDynamic;
import lsieun.bytecode.classfile.cp.ConstantFieldref;
import lsieun.bytecode.classfile.cp.ConstantFloat;
import lsieun.bytecode.classfile.cp.ConstantInteger;
import lsieun.bytecode.classfile.cp.ConstantInterfaceMethodref;
import lsieun.bytecode.classfile.cp.ConstantInvokeDynamic;
import lsieun.bytecode.classfile.cp.ConstantLong;
import lsieun.bytecode.classfile.cp.ConstantMethodHandle;
import lsieun.bytecode.classfile.cp.ConstantMethodType;
import lsieun.bytecode.classfile.cp.ConstantMethodref;
import lsieun.bytecode.classfile.cp.ConstantModule;
import lsieun.bytecode.classfile.cp.ConstantNameAndType;
import lsieun.bytecode.classfile.cp.ConstantPackage;
import lsieun.bytecode.classfile.cp.ConstantString;
import lsieun.bytecode.classfile.cp.ConstantUtf8;

public interface Visitor {
    void visitMagicNumber(MagicNumber obj);

    void visitMinorVersion(MinorVersion obj);

    void visitMajorVersion(MajorVersion obj);

    void visitConstantPoolCount(ConstantPoolCount obj);

    void visitConstantPool(ConstantPool obj);

    // region constant pool
    void visitConstantUtf8(ConstantUtf8 obj);

    void visitConstantInteger(ConstantInteger obj);

    void visitConstantFloat(ConstantFloat obj);

    void visitConstantLong(ConstantLong obj);

    void visitConstantDouble(ConstantDouble obj);

    void visitConstantClass(ConstantClass obj);

    void visitConstantString(ConstantString obj);

    void visitConstantFieldref(ConstantFieldref obj);

    void visitConstantMethodref(ConstantMethodref obj);

    void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj);

    void visitConstantNameAndType(ConstantNameAndType obj);

    void visitConstantMethodHandle(ConstantMethodHandle obj);

    void visitConstantMethodType(ConstantMethodType obj);

    void visitConstantDynamic(ConstantDynamic obj);

    void visitConstantInvokeDynamic(ConstantInvokeDynamic obj);

    void visitConstantModule(ConstantModule obj);

    void visitConstantPackage(ConstantPackage obj);
    // endregion
}