package lsieun.bytecode.classfile.cp;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.Node;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public abstract class Constant extends Node {
    private final byte tag;
    private int index;
    private String value;

    Constant(final byte tag) {
        this.tag = tag;
    }

    public byte getTag() {
        return tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // FIXME: 2,要进行混合，
    //  Class->Utf8
    //  String->Utf8
    //  NameAndType->Utf8
    //  MethodType->Utf8
    //  Module->Utf8
    //  Dynamic->NameAndType
    //  MemberRef->Class,NameAndType
    //  MethodHandle-> Ref
    public static Constant readConstant(final ByteDashboard byteDashboard) {
        final byte tag = byteDashboard.peek();

        switch (tag) {
            case CPConst.CONSTANT_Utf8:
                return new ConstantUtf8(byteDashboard);
            case CPConst.CONSTANT_Integer:
                return new ConstantInteger(byteDashboard);
            case CPConst.CONSTANT_Float:
                return new ConstantFloat(byteDashboard);
            case CPConst.CONSTANT_Long:
                return new ConstantLong(byteDashboard);
            case CPConst.CONSTANT_Double:
                return new ConstantDouble(byteDashboard);
            case CPConst.CONSTANT_Class:
                return new ConstantClass(byteDashboard);
            case CPConst.CONSTANT_String:
                return new ConstantString(byteDashboard);
            case CPConst.CONSTANT_Fieldref:
                return new ConstantFieldref(byteDashboard);
            case CPConst.CONSTANT_Methodref:
                return new ConstantMethodref(byteDashboard);
            case CPConst.CONSTANT_InterfaceMethodref:
                return new ConstantInterfaceMethodref(byteDashboard);
            case CPConst.CONSTANT_NameAndType:
                return new ConstantNameAndType(byteDashboard);
            case CPConst.CONSTANT_MethodHandle:
                return new ConstantMethodHandle(byteDashboard);
            case CPConst.CONSTANT_MethodType:
                return new ConstantMethodType(byteDashboard);
            case CPConst.CONSTANT_Dynamic:
                return new ConstantDynamic(byteDashboard);
            case CPConst.CONSTANT_InvokeDynamic:
                return new ConstantInvokeDynamic(byteDashboard);
            case CPConst.CONSTANT_Module:
                return new ConstantModule(byteDashboard);
            case CPConst.CONSTANT_Package:
                return new ConstantPackage(byteDashboard);
            default:
                throw new ClassFormatException("Invalid byte tag in constant pool: " + tag);
        }
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstant(this);
    }
}
