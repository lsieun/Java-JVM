package lsieun.bytecode.classfile;

import lsieun.utils.StringUtils;

public class JavaClass {
    private MagicNumber magicNumber;
    private MinorVersion minorVersion;
    private MajorVersion majorVersion;
    private ConstantPoolCount constantPoolCount;
    private ConstantPool constantPool;
    private AccessFlags accessFlags;
    private ThisClass thisClass;
    private SuperClass superClass;
    private InterfacesCount interfacesCount;
    private Interfaces interfaces;
    private FieldsCount fieldsCount;
    private Fields fields;
    private MethodsCount methodsCount;
    private Methods methods;
    private AttributesCount attributesCount;
    private Attributes attributes;

    // region getters and setters
    public MagicNumber getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(MagicNumber magicNumber) {
        this.magicNumber = magicNumber;
    }

    public MinorVersion getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(MinorVersion minorVersion) {
        this.minorVersion = minorVersion;
    }

    public MajorVersion getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(MajorVersion majorVersion) {
        this.majorVersion = majorVersion;
    }

    public ConstantPoolCount getConstantPoolCount() {
        return constantPoolCount;
    }

    public void setConstantPoolCount(ConstantPoolCount constantPoolCount) {
        this.constantPoolCount = constantPoolCount;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    public AccessFlags getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(AccessFlags accessFlags) {
        this.accessFlags = accessFlags;
    }

    public ThisClass getThisClass() {
        return thisClass;
    }

    public void setThisClass(ThisClass thisClass) {
        this.thisClass = thisClass;
    }

    public SuperClass getSuperClass() {
        return superClass;
    }

    public void setSuperClass(SuperClass superClass) {
        this.superClass = superClass;
    }

    public InterfacesCount getInterfacesCount() {
        return interfacesCount;
    }

    public void setInterfacesCount(InterfacesCount interfacesCount) {
        this.interfacesCount = interfacesCount;
    }

    public Interfaces getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Interfaces interfaces) {
        this.interfaces = interfaces;
    }

    public FieldsCount getFieldsCount() {
        return fieldsCount;
    }

    public void setFieldsCount(FieldsCount fieldsCount) {
        this.fieldsCount = fieldsCount;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public MethodsCount getMethodsCount() {
        return methodsCount;
    }

    public void setMethodsCount(MethodsCount methodsCount) {
        this.methodsCount = methodsCount;
    }

    public Methods getMethods() {
        return methods;
    }

    public void setMethods(Methods methods) {
        this.methods = methods;
    }

    public AttributesCount getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(AttributesCount attributesCount) {
        this.attributesCount = attributesCount;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    // endregion

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("ClassFile:" + StringUtils.LF)
        .append(magicNumber + StringUtils.LF)
        .append(minorVersion + StringUtils.LF)
        .append(majorVersion + StringUtils.LF)
        .append(constantPoolCount + StringUtils.LF)
        .append(constantPool + StringUtils.LF)
        .append(accessFlags + StringUtils.LF)
        .append(thisClass + StringUtils.LF)
        .append(superClass + StringUtils.LF)
        .append(interfacesCount + StringUtils.LF)
        .append(interfaces + StringUtils.LF)
        .append(fieldsCount + StringUtils.LF)
        .append(fields + StringUtils.LF)
        .append(methodsCount + StringUtils.LF)
        .append(methods + StringUtils.LF)
        .append(attributesCount + StringUtils.LF)
        .append(attributes + StringUtils.LF);
        return buff.toString();
    }
}
