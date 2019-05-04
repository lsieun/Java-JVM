package lsieun.bytecode.classfile;

import lsieun.bytecode.classfile.cp.Constant;
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
    void visitClassFile(ClassFile obj);

    void visitMagicNumber(MagicNumber obj);

    void visitMinorVersion(MinorVersion obj);

    void visitMajorVersion(MajorVersion obj);

    void visitConstantPoolCount(ConstantPoolCount obj);

    void visitConstantPool(ConstantPool obj);

    void visitAccessFlags(AccessFlags obj);

    void visitThisClass(ThisClass obj);

    void visitSuperClass(SuperClass obj);

    void visitInterfacesCount(InterfacesCount obj);

    void visitInterfaces(Interfaces obj);

    void visitFieldsCount(FieldsCount obj);

    void visitFields(Fields obj);

    void visitFieldInfo(FieldInfo obj);

    void visitMethodsCount(MethodsCount obj);

    void visitMethods(Methods obj);

    void visitMethodInfo(MethodInfo obj);

    void visitAttributesCount(AttributesCount obj);

    void visitAttributes(Attributes obj);

    void visitAttributeInfo(AttributeInfo obj);

    // region constant pool
    void visitConstant(Constant obj);

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
