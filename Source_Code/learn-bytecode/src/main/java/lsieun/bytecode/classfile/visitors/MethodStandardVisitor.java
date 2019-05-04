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
import lsieun.bytecode.classfile.attrs.Code;
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

public class MethodStandardVisitor implements Visitor {
    private String nameAndType;

    public MethodStandardVisitor(String nameAndType) {
        this.nameAndType = nameAndType;
    }

    @Override
    public void visitClassFile(ClassFile obj) {
        System.out.println("\n");
        ConstantPool constantPool = obj.getConstantPool();
        constantPool.accept(this);

        Methods methods = obj.getMethods();
        MethodInfo methodInfo = methods.findByNameAndType(nameAndType);
        if(methodInfo != null) {
            methodInfo.accept(this);
        }
        else {
            //
        }
    }

    @Override
    public void visitMagicNumber(MagicNumber obj) {

    }

    @Override
    public void visitMinorVersion(MinorVersion obj) {

    }

    @Override
    public void visitMajorVersion(MajorVersion obj) {

    }

    @Override
    public void visitConstantPoolCount(ConstantPoolCount obj) {

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

    }

    @Override
    public void visitThisClass(ThisClass obj) {

    }

    @Override
    public void visitSuperClass(SuperClass obj) {

    }

    @Override
    public void visitInterfacesCount(InterfacesCount obj) {

    }

    @Override
    public void visitInterfaces(Interfaces obj) {

    }

    @Override
    public void visitFieldsCount(FieldsCount obj) {

    }

    @Override
    public void visitFields(Fields obj) {

    }

    @Override
    public void visitFieldInfo(FieldInfo obj) {

    }

    @Override
    public void visitMethodsCount(MethodsCount obj) {

    }

    @Override
    public void visitMethods(Methods obj) {

    }

    @Override
    public void visitMethodInfo(MethodInfo obj) {
        List<AttributeInfo> attributesList = obj.getAttributesList();
        String attrNames = AttributeInfo.getAttributesName(attributesList);

        String line = String.format("MethodInfo {\n    MethodName='%s'\n    AccessFlags='%s'\n    Attrs='%s'\n}",
                obj.getValue(),
                obj.getAccessFlagsString(),
                attrNames);
        System.out.println(line);

        if(attributesList != null && attributesList.size() > 0) {
            for(AttributeInfo item : attributesList) {
                item.accept(this);
            }
        }
    }


    @Override
    public void visitAttributesCount(AttributesCount obj) {

    }

    @Override
    public void visitAttributes(Attributes obj) {

    }

    @Override
    public void visitAttributeInfo(AttributeInfo obj) {
        if(obj instanceof Code) {
            System.out.println("Code");
            Code code = (Code) obj;
        }
        else {
            String line = String.format("%s {attribute_name_index='%s', attribute_length='%d', HexCode='%s'}",
                    obj.getName(),
                    obj.getAttributeNameIndex(),
                    obj.getAttributeLength(),
                    obj.getHexCode());
            System.out.println(line);
        }

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
