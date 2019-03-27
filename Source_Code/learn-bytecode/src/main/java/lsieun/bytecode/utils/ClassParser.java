package lsieun.bytecode.utils;

import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.AccessFlags;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.AttributesCount;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.ConstantPoolCount;
import lsieun.bytecode.classfile.Fields;
import lsieun.bytecode.classfile.FieldsCount;
import lsieun.bytecode.classfile.Interfaces;
import lsieun.bytecode.classfile.InterfacesCount;
import lsieun.bytecode.classfile.MagicNumber;
import lsieun.bytecode.classfile.MajorVersion;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.MethodsCount;
import lsieun.bytecode.classfile.MinorVersion;
import lsieun.bytecode.classfile.SuperClass;
import lsieun.bytecode.classfile.ThisClass;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.bytecode.utils.ByteDashboard;

public class ClassParser {
    private ByteDashboard byteDashboard;

    public ClassParser(ByteDashboard byteDashboard) {
        this.byteDashboard = byteDashboard;
    }

    public ClassFile parse() {
        // Check magic tag of class file
        MagicNumber magicNumber = readMagicNumber();
        // Get compiler version
        MinorVersion minorVersion = readMinorVersion();
        MajorVersion majorVersion = readMajorVersion();
        // Read constant pool entries
        ConstantPoolCount constantPoolCount = readConstantPoolCount();
        ConstantPool constantPool = readConstantPool(constantPoolCount.getValue());
        constantPool.merge();
        // Read class information
        AccessFlags accessFlags = readAccessFlags();
        ThisClass thisClass = readThisClass();
        SuperClass superClass = readSuperClass();
        // Read interface information, i.e., implemented interfaces
        InterfacesCount interfacesCount = readInterfacesCount();
        Interfaces interfaces = readInterfaces(interfacesCount.getValue());
        // Read class fields, i.e., the variables of the class
        FieldsCount fieldsCount = readFieldsCount();
        Fields fields = readFields(fieldsCount.getValue(), constantPool);
        // Read class methods, i.e., the functions in the class
        MethodsCount methodsCount = readMethodsCount();
        Methods methods = readMethods(methodsCount.getValue(), constantPool);
        // Read class attributes
        AttributesCount attributesCount = readAttributesCount();
        Attributes attributes = readAttributes(attributesCount.getValue(), constantPool);

        ClassFile classFile = new ClassFile();
        classFile.setMagicNumber(magicNumber);
        classFile.setMinorVersion(minorVersion);
        classFile.setMajorVersion(majorVersion);
        classFile.setConstantPoolCount(constantPoolCount);
        classFile.setConstantPool(constantPool);
        classFile.setAccessFlags(accessFlags);
        classFile.setThisClass(thisClass);
        classFile.setSuperClass(superClass);
        classFile.setInterfacesCount(interfacesCount);
        classFile.setInterfaces(interfaces);
        classFile.setFieldsCount(fieldsCount);
        classFile.setFields(fields);
        classFile.setMethodsCount(methodsCount);
        classFile.setMethods(methods);
        classFile.setAttributesCount(attributesCount);
        classFile.setAttributes(attributes);

        return classFile;
    }

    /**
     * Check whether the header of the file is ok.
     * Of course, this has to be the first action on successive file reads.
     * @throws  ClassFormatException
     */
    private MagicNumber readMagicNumber() {
        byte[] bytes = this.byteDashboard.nextN(4);
        MagicNumber instance = new MagicNumber(bytes);
        return instance;
    }

    private MinorVersion readMinorVersion() {
        byte[] bytes = this.byteDashboard.nextN(2);

        MinorVersion instance = new MinorVersion(bytes);
        return instance;
    }

    private MajorVersion  readMajorVersion() {
        byte[] bytes = this.byteDashboard.nextN(2);

        MajorVersion instance = new MajorVersion(bytes);
        return instance;
    }

    private ConstantPoolCount readConstantPoolCount() {
        byte[] bytes = this.byteDashboard.nextN(2);

        ConstantPoolCount instance = new ConstantPoolCount(bytes);
        return instance;
    }

    private ConstantPool readConstantPool(int count) {
        ConstantPool instance = new ConstantPool(this.byteDashboard, count);
        return instance;
    }

    private AccessFlags readAccessFlags() {
        byte[] bytes = this.byteDashboard.nextN(2);

        AccessFlags instance = new AccessFlags(bytes);
        return instance;
    }

    private ThisClass readThisClass() {
        byte[] bytes = this.byteDashboard.nextN(2);

        ThisClass instance = new ThisClass(bytes);
        return instance;
    }

    private SuperClass readSuperClass() {
        byte[] bytes = this.byteDashboard.nextN(2);

        SuperClass instance = new SuperClass(bytes);
        return instance;
    }

    private InterfacesCount readInterfacesCount() {
        byte[] bytes = this.byteDashboard.nextN(2);

        InterfacesCount instance = new InterfacesCount(bytes);
        return instance;
    }

    private Interfaces readInterfaces(int count) {
        Interfaces instance = new Interfaces(this.byteDashboard, count);
        return instance;
    }

    private FieldsCount readFieldsCount() {
        byte[] bytes = this.byteDashboard.nextN(2);

        FieldsCount instance = new FieldsCount(bytes);
        return instance;
    }

    private Fields readFields(int count, ConstantPool constantPool) {
        Fields instance = new Fields(this.byteDashboard, count, constantPool);
        return instance;
    }

    private MethodsCount readMethodsCount() {
        byte[] bytes = this.byteDashboard.nextN(2);

        MethodsCount instance = new MethodsCount(bytes);
        return instance;
    }

    private Methods readMethods(int count, ConstantPool constantPool) {
        Methods instance = new Methods(this.byteDashboard, count, constantPool);
        return instance;
    }

    private AttributesCount readAttributesCount() {
        byte[] bytes = this.byteDashboard.nextN(2);

        AttributesCount instance = new AttributesCount(bytes);
        return instance;
    }

    private Attributes readAttributes(int count, ConstantPool constantPool) {
        Attributes instance = new Attributes(this.byteDashboard, count, constantPool);
        return instance;
    }
}
