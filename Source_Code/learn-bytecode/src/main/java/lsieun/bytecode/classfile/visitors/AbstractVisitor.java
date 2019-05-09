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
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.attrs.Deprecated;
import lsieun.bytecode.classfile.attrs.RuntimeVisibleAnnotations;
import lsieun.bytecode.classfile.attrs.Signature;
import lsieun.bytecode.classfile.attrs.classfile.InnerClasses;
import lsieun.bytecode.classfile.attrs.classfile.SourceFile;
import lsieun.bytecode.classfile.attrs.code.LineNumberTable;
import lsieun.bytecode.classfile.attrs.code.LocalVariableTable;
import lsieun.bytecode.classfile.attrs.code.LocalVariableTypeTable;
import lsieun.bytecode.classfile.attrs.code.StackMapTable;
import lsieun.bytecode.classfile.attrs.field.ConstantValue;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.attrs.method.Exceptions;
import lsieun.bytecode.generic.cnst.CPConst;
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
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.HexUtils;

public abstract class AbstractVisitor implements Visitor {

    // region ClassFile
    public void visitClassFile(ClassFile obj){
        // do nothing
    }

    public void visitMagicNumber(MagicNumber obj){
        // do nothing
    }

    public void visitMinorVersion(MinorVersion obj){
        // do nothing
    }

    public void visitMajorVersion(MajorVersion obj){
        // do nothing
    }

    public void visitConstantPoolCount(ConstantPoolCount obj){
        // do nothing
    }

    public void visitConstantPool(ConstantPool obj){
        System.out.println("ConstantPool {");
        for(int i=0; i<obj.getEntries().length; i++) {
            Constant item = obj.getEntries()[i];
            if(item == null) continue;
            item.accept(this);
        }
        System.out.println("}");
    }

    public void visitAccessFlags(AccessFlags obj){
        // do nothing
    }

    public void visitThisClass(ThisClass obj){
        // do nothing
    }

    public void visitSuperClass(SuperClass obj){
        // do nothing
    }

    public void visitInterfacesCount(InterfacesCount obj){
        // do nothing
    }

    public void visitInterfaces(Interfaces obj){
        // do nothing
    }

    public void visitFieldsCount(FieldsCount obj){
        // do nothing
    }

    public void visitFields(Fields obj){
        // do nothing
    }

    public void visitFieldInfo(FieldInfo obj){
        // do nothing
    }

    public void visitMethodsCount(MethodsCount obj){
        // do nothing
    }

    public void visitMethods(Methods obj){
        // do nothing
    }

    public void visitMethodInfo(MethodInfo obj){
        // do nothing
    }

    public void visitAttributesCount(AttributesCount obj){
        // do nothing
    }

    public void visitAttributes(Attributes obj){
        AttributeInfo[] entries = obj.getEntries();
        if(entries != null && entries.length > 0) {
            for(AttributeInfo item : entries) {
                item.accept(this);
            }
        }
    }

    public void visitAttributeInfo(AttributeInfo obj){
        byte[] bytes = obj.getBytes();
        ByteDashboard byteDashboard = new ByteDashboard("AttributeInfo", bytes);
        byte[] attribute_name_index_bytes = byteDashboard.nextN(2);
        byte[] attribute_length_bytes = byteDashboard.nextN(4);


        String line = String.format("%s {name_index='0x%s'(%d), length='0x%s'(%d), HexCode='%s'}",
                obj.getName(),
                HexUtils.fromBytes(attribute_name_index_bytes), obj.getAttributeNameIndex(),
                HexUtils.fromBytes(attribute_length_bytes), obj.getAttributeLength(),
                obj.getHexCode());
        System.out.println(line);
    }
    // endregion

    // region constant pool
    public void visitConstant(Constant obj){
        String line = String.format("    |%03d| %s {Value='%s', HexCode='%s'}",
                obj.getIndex(),
                CPConst.getConstantName(obj.getTag()),
                obj.getValue(),
                obj.getHexCode());
        System.out.println(line);
    }

    public void visitConstantUtf8(ConstantUtf8 obj){
        visitConstant(obj);
    }

    public void visitConstantInteger(ConstantInteger obj){
        visitConstant(obj);
    }

    public void visitConstantFloat(ConstantFloat obj){
        visitConstant(obj);
    }

    public void visitConstantLong(ConstantLong obj){
        visitConstant(obj);
    }

    public void visitConstantDouble(ConstantDouble obj){
        visitConstant(obj);
    }

    public void visitConstantClass(ConstantClass obj){
        visitConstant(obj);
    }

    public void visitConstantString(ConstantString obj){
        visitConstant(obj);
    }

    public void visitConstantFieldref(ConstantFieldref obj){
        visitConstant(obj);
    }

    public void visitConstantMethodref(ConstantMethodref obj){
        visitConstant(obj);
    }

    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj){
        visitConstant(obj);
    }

    public void visitConstantNameAndType(ConstantNameAndType obj){
        visitConstant(obj);
    }

    public void visitConstantMethodHandle(ConstantMethodHandle obj){
        visitConstant(obj);
    }

    public void visitConstantMethodType(ConstantMethodType obj){
        visitConstant(obj);
    }

    public void visitConstantDynamic(ConstantDynamic obj){
        visitConstant(obj);
    }

    public void visitConstantInvokeDynamic(ConstantInvokeDynamic obj){
        visitConstant(obj);
    }

    public void visitConstantModule(ConstantModule obj){
        visitConstant(obj);
    }

    public void visitConstantPackage(ConstantPackage obj){
        visitConstant(obj);
    }
    // endregion

    // region attributes
    public void visitCode(Code obj){
        visitAttributeInfo(obj);
    }

    public void visitConstantValue(ConstantValue obj){
        visitAttributeInfo(obj);
    }

    public void visitDeprecated(Deprecated obj){
        visitAttributeInfo(obj);
    }

    public void visitExceptions(Exceptions obj){
        visitAttributeInfo(obj);
    }

    public void visitInnerClasses(InnerClasses obj){
        visitAttributeInfo(obj);
    }

    public void visitLineNumberTable(LineNumberTable obj){
        visitAttributeInfo(obj);
    }

    public void visitLocalVariableTable(LocalVariableTable obj){
        visitAttributeInfo(obj);
    }

    public void visitLocalVariableTypeTable(LocalVariableTypeTable obj){
        visitAttributeInfo(obj);
    }

    public void visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations obj){
        visitAttributeInfo(obj);
    }

    public void visitSignature(Signature obj){
        visitAttributeInfo(obj);
    }

    public void visitSourceFile(SourceFile obj){
        visitAttributeInfo(obj);
    }

    public void visitStackMapTable(StackMapTable obj){
        visitAttributeInfo(obj);
    }
    // endregion


}
