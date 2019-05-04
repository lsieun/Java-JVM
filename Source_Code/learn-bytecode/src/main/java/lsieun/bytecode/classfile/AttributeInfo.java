package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.attrs.Code;
import lsieun.bytecode.classfile.attrs.ConstantValue;
import lsieun.bytecode.classfile.attrs.Deprecated;
import lsieun.bytecode.classfile.attrs.Exceptions;
import lsieun.bytecode.classfile.attrs.InnerClasses;
import lsieun.bytecode.classfile.attrs.LineNumberTable;
import lsieun.bytecode.classfile.attrs.LocalVariableTable;
import lsieun.bytecode.classfile.attrs.LocalVariableTypeTable;
import lsieun.bytecode.classfile.attrs.RuntimeVisibleAnnotations;
import lsieun.bytecode.classfile.attrs.Signature;
import lsieun.bytecode.classfile.attrs.SourceFile;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public class AttributeInfo extends Node {
    private final int attribute_name_index;
    private final int attribute_length;
    private final byte[] info;

    private final String name;

    public AttributeInfo(ByteDashboard byteDashboard, ConstantPool constantPool, boolean isPeek) {
        byte[] attribute_name_index_bytes = byteDashboard.nextN(2);
        byte[] attribute_length_bytes = byteDashboard.nextN(4);

        this.attribute_name_index = ByteUtils.bytesToInt(attribute_name_index_bytes, 0);
        this.attribute_length = ByteUtils.bytesToInt(attribute_length_bytes, 0);
        if(isPeek) {
            this.info = byteDashboard.peekN(attribute_length);
        }
        else {
            this.info = byteDashboard.nextN(attribute_length);
        }

        this.name = constantPool.getConstantString(attribute_name_index, CPConst.CONSTANT_Utf8);
        byte[] bytes = ByteUtils.merge(attribute_name_index_bytes, attribute_length_bytes, info);
        super.setBytes(bytes);
    }

    public AttributeInfo(ByteDashboard byteDashboard, ConstantPool constantPool) {
        this(byteDashboard, constantPool, false);
    }

    public int getAttributeNameIndex() {
        return attribute_name_index;
    }

    public int getAttributeLength() {
        return attribute_length;
    }

    public byte[] getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public static String getAttributesName(List<AttributeInfo> attributesList) {
        List<String> attr_list = new ArrayList();
        for(int i=0; i<attributesList.size(); i++) {
            AttributeInfo item = attributesList.get(i);
            String name = item.getName();
            attr_list.add(name);
        }

        String attrNames = StringUtils.list2str(attr_list, "[", "]", ",");
        if(attrNames == null) {
            attrNames = "[]";
        }
        return attrNames;
    }

    public static AttributeInfo read(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] attribute_name_index_bytes = byteDashboard.peekN(2);
        int attributeNameIndex = ByteUtils.bytesToInt(attribute_name_index_bytes, 0);

        String name = constantPool.getConstantString(attributeNameIndex, CPConst.CONSTANT_Utf8);
        AttributeInfo instance = null;

        if("SourceFile".equals(name)) {
            instance = new SourceFile(byteDashboard, constantPool);
        }
        else if("InnerClasses".equals(name)) {
            instance = new InnerClasses(byteDashboard, constantPool);
        }
        else if("Code".equals(name)) {
            instance = new Code(byteDashboard, constantPool);
        }
        else if("LineNumberTable".equals(name)) {
            instance = new LineNumberTable(byteDashboard, constantPool);
        }
        else if("LocalVariableTable".equals(name)) {
            instance = new LocalVariableTable(byteDashboard, constantPool);
        }
        else if("LocalVariableTypeTable".equals(name)) {
            instance = new LocalVariableTypeTable(byteDashboard, constantPool);
        }
        else if("Signature".equals(name)) {
            instance = new Signature(byteDashboard, constantPool);
        }
        else if("Deprecated".equals(name)) {
            instance = new Deprecated(byteDashboard, constantPool);
        }
        else if("Exceptions".equals(name)) {
            instance = new Exceptions(byteDashboard, constantPool);
        }
        else if("ConstantValue".equals(name)) {
            instance = new ConstantValue(byteDashboard, constantPool);
        }
        else if("RuntimeVisibleAnnotations".equals(name)) {
            instance = new RuntimeVisibleAnnotations(byteDashboard, constantPool);
        }
        else {
            instance = new AttributeInfo(byteDashboard, constantPool);
        }


        return instance;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitAttributeInfo(this);
    }

}
