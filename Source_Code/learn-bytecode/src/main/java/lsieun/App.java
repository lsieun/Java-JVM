package lsieun;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.visitors.AttributeStandardVisitor;
import lsieun.bytecode.classfile.visitors.ClassFileStandardVisitor;
import lsieun.bytecode.classfile.visitors.FieldStandardVisitor;
import lsieun.bytecode.classfile.visitors.MethodStandardVisitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.ClassParser;
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

        final String useJar = PropertyUtils.getProperty("classfile.source.use.jar");
        if ("true".equalsIgnoreCase(useJar)) {
            String jarPath = PropertyUtils.getProperty("classfile.source.jar.path");
            String entryName = PropertyUtils.getProperty("classfile.source.jar.entry.name");
            url = "jar:file:" + jarPath + "!/" + entryName;
            bytes = JarUtils.readBytes(jarPath, entryName);
        } else {
            String rootPath = PropertyUtils.getRootPath();
            String filepath = rootPath + PropertyUtils.getProperty("classfile.source.file.path.relative");
            url = "file://" + filepath;
            bytes = FileUtils.readBytes(filepath);
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
        String target = PropertyUtils.getProperty("classfile.content.target");
        if("ClassFile".equalsIgnoreCase(target)) {
            displayClassFile(classFile);
        }
        else if("Field".equalsIgnoreCase(target)) {
            String fieldSignature = PropertyUtils.getProperty("classfile.content.field.signature");
            displayField(classFile, fieldSignature);
        }
        else if("Method".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("classfile.content.method.signature");
            displayMethod(classFile, methodSignature);
        }
        else if("ClassFileAttribute".equalsIgnoreCase(target)) {
            String attrName = PropertyUtils.getProperty("classfile.content.attribute.name");
            displayClassFileAttribute(classFile, attrName);
        }
        else if("FieldAttribute".equalsIgnoreCase(target)) {
            String fieldSignature = PropertyUtils.getProperty("classfile.content.field.signature");
            String attrName = PropertyUtils.getProperty("classfile.content.attribute.name");
            displayFieldAttribute(classFile, fieldSignature, attrName);
        }
        else if("MethodAttribute".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("classfile.content.method.signature");
            String attrName = PropertyUtils.getProperty("classfile.content.attribute.name");
            displayMethodAttribute(classFile, methodSignature, attrName);
        }
        else if("CodeAttribute".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("classfile.content.method.signature");
            String attrName = PropertyUtils.getProperty("classfile.content.attribute.name");
            displayCodeAttribute(classFile, methodSignature, attrName);
        }
        else {
            System.out.println("please change 'classfile.content.target' in config.properties files");
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

    public static void displayClassFileAttribute(ClassFile classFile, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Attributes attributes = classFile.getAttributes();
        displayAttribute(classFile, attributes, attrName);
    }

    public static void displayFieldAttribute(ClassFile classFile, String nameAndType, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Fields fields = classFile.getFields();
        FieldInfo fieldInfo = fields.findByNameAndType(nameAndType);
        if(fieldInfo != null) {
            Attributes attributes = fieldInfo.getAttributes();
            displayAttribute(classFile, attributes, attrName);
        }
    }

    public static void displayMethodAttribute(ClassFile classFile, String nameAndType, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Methods methods = classFile.getMethods();
        MethodInfo methodInfo = methods.findByNameAndType(nameAndType);
        if(methodInfo != null) {
            Attributes attributes = methodInfo.getAttributes();
            displayAttribute(classFile, attributes, attrName);
        }
    }

    public static void displayCodeAttribute(ClassFile classFile, String nameAndType, String attrName) {
        System.out.println("=================================================" + StringUtils.LF);
        Methods methods = classFile.getMethods();
        MethodInfo methodInfo = methods.findByNameAndType(nameAndType);
        if(methodInfo == null) return;

        Attributes attributes = methodInfo.getAttributes();
        AttributeInfo attributeInfo = attributes.findAttribute("Code");
        if(attributeInfo == null) return;

        Code code = (Code) attributeInfo;
        displayAttribute(classFile, code.getAttributes(), attrName);
    }

    public static void displayAttribute(ClassFile classFile, Attributes attributes, String attrName) {
        AttributeInfo attributeInfo = attributes.findAttribute(attrName);
        if(attributeInfo != null) {
            AttributeStandardVisitor visitor = new AttributeStandardVisitor(true);
            classFile.accept(visitor);
            attributeInfo.accept(visitor);
        }
    }
    // endregion
}
