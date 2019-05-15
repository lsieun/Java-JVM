package lsieun;

import java.io.InputStream;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.visitors.AttributeClassFileVisitor;
import lsieun.bytecode.classfile.visitors.AttributeFieldVisitor;
import lsieun.bytecode.classfile.visitors.AttributeMethodVisitor;
import lsieun.bytecode.classfile.visitors.ClassFileStandardVisitor;
import lsieun.bytecode.classfile.visitors.AttributeCodeVisitor;
import lsieun.bytecode.classfile.visitors.FieldStandardVisitor;
import lsieun.bytecode.classfile.visitors.MethodStandardVisitor;
import lsieun.bytecode.generic.instruction.visitor.CodeStandardVisitor;
import lsieun.bytecode.generic.instruction.visitor.StackMapFrameVisitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.ClassParser;
import lsieun.bytecode.utils.clazz.AttributeUtils;
import lsieun.bytecode.utils.clazz.FieldUtils;
import lsieun.bytecode.utils.clazz.MethodUtils;
import lsieun.bytecode.utils.PropertyUtils;
import lsieun.utils.StringUtils;
import lsieun.utils.io.FileUtils;
import lsieun.utils.io.JarUtils;

public class App {
    public static void main(String[] args) {
        //(1)read bytecode
        ByteDashboard byteDashboard = readBytes();

        //(2)bytecode-->ClassFile
        ClassParser classParser = new ClassParser(byteDashboard);
        ClassFile classFile = classParser.parse();

        //(3)display ClassFile
        display(classFile);
    }

    public static ByteDashboard readBytes() {
        String url = null;
        byte[] bytes = null;

        final String type = PropertyUtils.getProperty("bytecode.source.type");
        if ("jar".equalsIgnoreCase(type)) {
            String jarPath = PropertyUtils.getProperty("bytecode.source.jar.path");
            String entryName = PropertyUtils.getProperty("bytecode.source.jar.entry.name");
            url = "jar:file:" + jarPath + "!/" + entryName;
            bytes = JarUtils.readBytes(jarPath, entryName);
        }
        else if ("filepath".equalsIgnoreCase(type)) {
            String filepath = PropertyUtils.getProperty("bytecode.source.file.filepath");
            url = "file://" + filepath;
            bytes = FileUtils.readBytes(filepath);
        }
        else if ("classname".equalsIgnoreCase(type)){
            String classname = PropertyUtils.getProperty("bytecode.source.class.fqcn");
            String filepath = FileUtils.getFilePath(App.class, classname);
            url = "file://" + filepath;
            bytes = FileUtils.readBytes(filepath);
        }
        else {
            String classname = PropertyUtils.getProperty("bytecode.source.classloader.fqcn");
            InputStream in = FileUtils.getInputStream(classname);
            bytes = FileUtils.readStream(in, true);
        }

        if(bytes == null) {
            throw new RuntimeException("bytes is null!!!");
        }

        ByteDashboard byteDashboard = new ByteDashboard(url, bytes);
        //System.out.println(byteDashboard);
        return byteDashboard;
    }

    public static void display(ClassFile classFile) {
        // 打印ClassFile
        String target = PropertyUtils.getProperty("bytecode.target.name");
        if("ClassFile".equalsIgnoreCase(target)) {
            displayClassFile(classFile);
        }
        else if("Field".equalsIgnoreCase(target)) {
            String fieldSignature = PropertyUtils.getProperty("bytecode.content.field.signature");
            displayField(classFile, fieldSignature);
        }
        else if("Method".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("bytecode.content.method.signature");
            displayMethod(classFile, methodSignature);
        }
        else if("Code".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("bytecode.content.method.signature");
            displayCode(classFile, methodSignature);
        }
        else if("StackMapTable".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("bytecode.content.method.signature");
            displayStackMapTable(classFile, methodSignature);
        }
        else if("ClassFileAttribute".equalsIgnoreCase(target)) {
            String attrName = PropertyUtils.getProperty("bytecode.content.classfile.attribute.name");
            displayClassFileAttribute(classFile, attrName);
        }
        else if("FieldAttribute".equalsIgnoreCase(target)) {
            String fieldSignature = PropertyUtils.getProperty("bytecode.content.field.signature");
            String attrName = PropertyUtils.getProperty("bytecode.content.field.attribute.name");
            displayFieldAttribute(classFile, fieldSignature, attrName);
        }
        else if("MethodAttribute".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("bytecode.content.method.signature");
            String attrName = PropertyUtils.getProperty("bytecode.content.method.attribute.name");
            displayMethodAttribute(classFile, methodSignature, attrName);
        }
        else if("CodeAttribute".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("bytecode.content.method.signature");
            String attrName = PropertyUtils.getProperty("bytecode.content.code.attribute.name");
            displayCodeAttribute(classFile, methodSignature, attrName);
        }
        else {
            System.out.println("please change 'bytecode.target.name' in config.properties files");
        }
    }

    // region display methods
    public static void displayClassFile(ClassFile classFile) {
        System.out.println("=================================================" + StringUtils.LF);
        ClassFileStandardVisitor visitor = new ClassFileStandardVisitor();
        classFile.accept(visitor);
    }

    public static void displayField(ClassFile classFile, String nameAndType) {
        System.out.println("=================================================" + StringUtils.LF);
        FieldStandardVisitor visitor = new FieldStandardVisitor(nameAndType);
        classFile.accept(visitor);
    }

    public static void displayMethod(ClassFile classFile, String nameAndType) {
        System.out.println("=================================================" + StringUtils.LF);
        MethodStandardVisitor visitor = new MethodStandardVisitor(nameAndType);
        //MethodRawVisitor visitor = new MethodRawVisitor(nameAndType);
        classFile.accept(visitor);
    }

    public static void displayCode(ClassFile classFile, String nameAndType) {
        System.out.println("=================================================" + StringUtils.LF);
        MethodInfo methodInfo = MethodUtils.findMethod(classFile, nameAndType);
        if(methodInfo == null) {
            MethodUtils.displayAvailableMethods(classFile);
            return;
        }

        Code code = AttributeUtils.findCodeAttribute(methodInfo);
        if(code == null) {
            return;
        }

        ConstantPool constantPool = classFile.getConstantPool();
        byte[] bytes = code.getCode();

        int descriptorIndex = methodInfo.getDescriptorIndex();
        String descriptor = constantPool.getConstant(descriptorIndex).getValue();
        System.out.println(descriptor);

        CodeStandardVisitor visitor = new CodeStandardVisitor();
        visitor.visit(constantPool, bytes);
    }

    public static void displayStackMapTable(ClassFile classFile, String nameAndType) {
        System.out.println("=================================================" + StringUtils.LF);
        MethodInfo methodInfo = MethodUtils.findMethod(classFile, nameAndType);
        if(methodInfo == null) {
            MethodUtils.displayAvailableMethods(classFile);
            return;
        }

        ConstantPool constantPool = classFile.getConstantPool();
        StackMapFrameVisitor visitor = new StackMapFrameVisitor();
        visitor.visit(methodInfo, constantPool);
    }

    public static void displayClassFileAttribute(ClassFile classFile, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Attributes attributes = classFile.getAttributes();
        AttributeInfo attributeInfo = AttributeUtils.findAttribute(attributes, attrName);
        if(attributeInfo != null) {
            AttributeClassFileVisitor visitor = new AttributeClassFileVisitor();
            attributeInfo.accept(visitor);
        }
    }

    public static void displayFieldAttribute(ClassFile classFile, String nameAndType, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Fields fields = classFile.getFields();
        FieldInfo fieldInfo = FieldUtils.findField(fields, nameAndType);
        if(fieldInfo != null) {
            Attributes attributes = fieldInfo.getAttributes();
            AttributeInfo attributeInfo = AttributeUtils.findAttribute(attributes, attrName);
            if(attributeInfo != null) {
                AttributeFieldVisitor visitor = new AttributeFieldVisitor();
                attributeInfo.accept(visitor);
            }
        }
    }

    public static void displayMethodAttribute(ClassFile classFile, String nameAndType, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Methods methods = classFile.getMethods();
        MethodInfo methodInfo = MethodUtils.findMethod(methods, nameAndType);
        if(methodInfo != null) {
            Attributes attributes = methodInfo.getAttributes();
            AttributeInfo attributeInfo = AttributeUtils.findAttribute(attributes, attrName);
            if(attributeInfo != null) {
                AttributeMethodVisitor visitor = new AttributeMethodVisitor();
                attributeInfo.accept(visitor);
            }
        }
    }

    public static void displayCodeAttribute(ClassFile classFile, String nameAndType, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Methods methods = classFile.getMethods();
        MethodInfo methodInfo = MethodUtils.findMethod(methods, nameAndType);
        if(methodInfo == null) {
            System.out.println("Can not find Method: " + nameAndType);
            System.out.println("Available Methods: " + MethodUtils.getMethodNames(methods));
            return;
        }

        Attributes attributes = methodInfo.getAttributes();
        AttributeInfo attributeInfo = AttributeUtils.findAttribute(attributes, "Code");
        if(attributeInfo == null) {
            System.out.println("Can not find Code Attribute");
            return;
        }


        Code code = (Code) attributeInfo;
        Attributes codeAttributes = code.getAttributes();
        AttributeInfo subAttribute = AttributeUtils.findAttribute(attributes, attrName);
        if(subAttribute == null) {
            System.out.println("Can not find Attribute: " + attrName);
            System.out.println("Available Attributes: " + AttributeUtils.getAttributeNames(codeAttributes));
            return;
        }

        AttributeCodeVisitor visitor = new AttributeCodeVisitor();
        subAttribute.accept(visitor);
    }

    // endregion
}
