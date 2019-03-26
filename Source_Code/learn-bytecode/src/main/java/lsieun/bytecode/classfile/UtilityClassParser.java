package lsieun.bytecode.classfile;

import lsieun.bytecode.classfile.basic.JVMConst;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class UtilityClassParser {
    private ByteDashboard byteDashboard;

    public UtilityClassParser(ByteDashboard byteDashboard) {
        this.byteDashboard = byteDashboard;
    }

    public JavaClass parse() {
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

        JavaClass javaClass = new JavaClass();
        javaClass.setMagicNumber(magicNumber);
        javaClass.setMinorVersion(minorVersion);
        javaClass.setMajorVersion(majorVersion);
        javaClass.setConstantPoolCount(constantPoolCount);
        javaClass.setConstantPool(constantPool);
        javaClass.setAccessFlags(accessFlags);
        javaClass.setThisClass(thisClass);
        javaClass.setSuperClass(superClass);
        javaClass.setInterfacesCount(interfacesCount);
        javaClass.setInterfaces(interfaces);
        javaClass.setFieldsCount(fieldsCount);
        javaClass.setFields(fields);
        javaClass.setMethodsCount(methodsCount);
        javaClass.setMethods(methods);
        javaClass.setAttributesCount(attributesCount);
        javaClass.setAttributes(attributes);

        return javaClass;
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
