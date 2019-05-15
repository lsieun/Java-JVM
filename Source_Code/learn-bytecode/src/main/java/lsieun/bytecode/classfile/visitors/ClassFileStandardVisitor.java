package lsieun.bytecode.classfile.visitors;

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
import lsieun.bytecode.classfile.attrs.classfile.SourceFile;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.clazz.AttributeUtils;
import lsieun.utils.radix.HexUtils;

public class ClassFileStandardVisitor extends AbstractVisitor {

    @Override
    public void visitClassFile(ClassFile obj) {
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
        Attributes attributes = obj.getAttributes();
        String attrNames = AttributeUtils.getAttributeNames(attributes);

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
        Attributes attributes = obj.getAttributes();
        String attrNames = AttributeUtils.getAttributeNames(attributes);

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
        AttributeInfo[] entries = obj.getEntries();
        if(entries != null && entries.length > 0) {
            System.out.println("Attributes {");
            System.out.println("    // 囿于打印空间，“多而易乱”，因此各个属性只打印“重要”信息");
            for(AttributeInfo item : entries) {
                item.accept(this);
            }
            System.out.println("}");
        }
    }

    // region attributes

    @Override
    public void visitSourceFile(SourceFile obj) {
        byte[] bytes = obj.getBytes();
        ByteDashboard byteDashboard = new ByteDashboard("AttributeInfo", bytes);
        byte[] sourcefile_index_bytes = byteDashboard.peekN(6, 2);

        String line = String.format("    %s {Value='%s', sourcefile_index='%s'(%d), HexCode='%s'}",
                obj.getName(),
                obj.getValue(),
                HexUtils.fromBytes(sourcefile_index_bytes),
                obj.getSourcefileIndex(),
                obj.getHexCode());
        System.out.println(line);
    }

    // endregion
}
