package lsieun.bytecode.classfile.visitors;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AccessFlags;
import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.AttributesCount;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.ConstantPoolCount;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
import lsieun.bytecode.classfile.FieldsCount;
import lsieun.bytecode.classfile.Interfaces;
import lsieun.bytecode.classfile.InterfacesCount;
import lsieun.bytecode.classfile.MagicNumber;
import lsieun.bytecode.classfile.MajorVersion;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.MethodsCount;
import lsieun.bytecode.classfile.MinorVersion;
import lsieun.bytecode.classfile.SuperClass;
import lsieun.bytecode.classfile.ThisClass;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
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
import lsieun.utils.StringUtils;

public class ClassFileStandardVisitor implements Visitor {

    @Override
    public void visitClassFile(ClassFile obj) {
        System.out.println("\n");
        MagicNumber magicNumber = obj.getMagicNumber();
        magicNumber.accept(this);

        MinorVersion minorVersion = obj.getMinorVersion();
        minorVersion.accept(this);

        MajorVersion majorVersion = obj.getMajorVersion();
        majorVersion.accept(this);

        ConstantPoolCount constantPoolCount = obj.getConstantPoolCount();
        constantPoolCount.accept(this);

        ConstantPool constantPool = obj.getConstantPool();
        constantPool.accept(this);

        AccessFlags accessFlags = obj.getAccessFlags();
        accessFlags.accept(this);

        ThisClass thisClass = obj.getThisClass();
        thisClass.accept(this);

        SuperClass superClass = obj.getSuperClass();
        superClass.accept(this);

        InterfacesCount interfacesCount = obj.getInterfacesCount();
        interfacesCount.accept(this);

        Interfaces interfaces = obj.getInterfaces();
        interfaces.accept(this);

        FieldsCount fieldsCount = obj.getFieldsCount();
        fieldsCount.accept(this);

        Fields fields = obj.getFields();
        fields.accept(this);

        MethodsCount methodsCount = obj.getMethodsCount();
        methodsCount.accept(this);

        Methods methods = obj.getMethods();
        methods.accept(this);

        AttributesCount attributesCount = obj.getAttributesCount();
        attributesCount.accept(this);

        Attributes attributes = obj.getAttributes();
        attributes.accept(this);
    }

    @Override
    public void visitMagicNumber(MagicNumber obj) {
        String line = String.format("MagicNumber {HexCode='%s'}", obj.getHexCode());
        System.out.println(line);
    }

    @Override
    public void visitMinorVersion(MinorVersion obj) {
        String line = String.format("MinorVersion {HexCode='%s'}", obj.getHexCode());
        System.out.println(line);
    }

    @Override
    public void visitMajorVersion(MajorVersion obj) {
        String line = String.format("MajorVersion {HexCode='%s', Compiler-Version='%s'}", obj.getHexCode(), obj.getJDKVersion());
        System.out.println(line);
    }

    @Override
    public void visitConstantPoolCount(ConstantPoolCount obj) {
        String line = String.format("ConstantPoolCount {HexCode='%s', Value='%d'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitConstantPool(ConstantPool obj) {
        System.out.println("ConstantPool {");
        for(int i=0; i<obj.getEntries().length; i++) {
            Constant item = obj.getEntries()[i];
            if(item == null) continue;
            item.accept(this);
        }
        System.out.println("}");
    }

    @Override
    public void visitAccessFlags(AccessFlags obj) {
        String line = String.format("AccessFlags {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitThisClass(ThisClass obj) {
        String line = String.format("ThisClass {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitSuperClass(SuperClass obj) {
        String line = String.format("SuperClass {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitInterfacesCount(InterfacesCount obj) {
        String line = String.format("InterfacesCount {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitInterfaces(Interfaces obj) {
        String line = String.format("Interfaces {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitFieldsCount(FieldsCount obj) {
        String line = String.format("FieldsCount {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitFields(Fields obj) {
        System.out.println("Fields {");
        for(int i = 0; i<obj.getEntries().length; i++) {
            FieldInfo item = obj.getEntries()[i];
            item.accept(this);
        }
        System.out.println("}");
    }

    @Override
    public void visitFieldInfo(FieldInfo obj) {
        List<AttributeInfo> attributesList = obj.getAttributesList();
        String attrNames = AttributeInfo.getAttributesName(attributesList);

        String line = String.format("    FieldInfo {Value='%s', AccessFlags='%s', Attrs='%s', HexCode='%s'}",
                obj.getValue(),
                obj.getAccessFlagsString(),
                attrNames,
                obj.getHexCode());
        System.out.println(line);
    }

    @Override
    public void visitMethodsCount(MethodsCount obj) {
        String line = String.format("MethodsCount {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitMethods(Methods obj) {
        System.out.println("Methods {");
        for(int i = 0; i<obj.getEntries().length; i++) {
            MethodInfo item = obj.getEntries()[i];
            item.accept(this);
        }
        System.out.println("}");
    }

    @Override
    public void visitMethodInfo(MethodInfo obj) {
        List<AttributeInfo> attributesList = obj.getAttributesList();
        String attrNames = AttributeInfo.getAttributesName(attributesList);

        String line = String.format("    MethodInfo {Value='%s', AccessFlags='%s', Attrs='%s'}",
                obj.getValue(),
                obj.getAccessFlagsString(),
                attrNames);
        System.out.println(line);
    }

    @Override
    public void visitAttributesCount(AttributesCount obj) {
        String line = String.format("AttributesCount {HexCode='%s', Value='%s'}", obj.getHexCode(), obj.getValue());
        System.out.println(line);
    }

    @Override
    public void visitAttributes(Attributes obj) {
        System.out.println("Attributes {");
        for(int i = 0; i<obj.getEntries().length; i++) {
            AttributeInfo item = obj.getEntries()[i];
            item.accept(this);
        }
        System.out.println("}");
    }

    @Override
    public void visitAttributeInfo(AttributeInfo obj) {
        String line = String.format("    %s {attribute_name_index='%s', attribute_length='%d', HexCode='%s'}",
                obj.getName(),
                obj.getAttributeNameIndex(),
                obj.getAttributeLength(),
                obj.getHexCode());
        System.out.println(line);
    }

    // region constant pool

    @Override
    public void visitConstant(Constant obj) {
        String line = String.format("    |%03d| %s {Value='%s', HexCode='%s'}",
                obj.getIndex(),
                CPConst.getConstantName(obj.getTag()),
                obj.getValue(),
                obj.getHexCode());
        System.out.println(line);
    }

    @Override
    public void visitConstantUtf8(ConstantUtf8 obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantInteger(ConstantInteger obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantFloat(ConstantFloat obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantLong(ConstantLong obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantDouble(ConstantDouble obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantClass(ConstantClass obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantString(ConstantString obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantFieldref(ConstantFieldref obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantMethodref(ConstantMethodref obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantNameAndType(ConstantNameAndType obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantMethodHandle(ConstantMethodHandle obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantMethodType(ConstantMethodType obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantDynamic(ConstantDynamic obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantInvokeDynamic(ConstantInvokeDynamic obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantModule(ConstantModule obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantPackage(ConstantPackage obj) {
        visitConstant(obj);
    }
    // endregion


}
