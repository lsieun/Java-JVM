package lsieun;

import java.util.List;

import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
import lsieun.bytecode.classfile.visitors.ClassFileStandardVisitor;
import lsieun.bytecode.classfile.visitors.MethodStandardVisitor;
import lsieun.bytecode.utils.ClassParser;
import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.utils.ByteDashboard;
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
        else if("Attribute".equalsIgnoreCase(target)) {
            displayClassFileAttribute(classFile);
        }
        else if("FieldAttribute".equalsIgnoreCase(target)) {
            // TODO
        }
        else if("MethodAttribute".equalsIgnoreCase(target)) {
            // TODO
        }
        else if("CodeAttribute".equalsIgnoreCase(target)) {
            // TODOMethod
        }
        else {
            System.out.println("please change 'classfile.content.target' in config.properties files");
        }


        //displayMethod(classFile, "testMethod:(ILjava/lang/String;)V");
    }

    // region display methods
    public static void displayClassFile(ClassFile classFile) {
        ClassFileStandardVisitor visitor = new ClassFileStandardVisitor();
        classFile.accept(visitor);
    }

    public static void displayField(ClassFile classFile, String nameAndType) {
        System.out.println("=================================================" + StringUtils.LF);
        System.out.println(classFile.getConstantPool() + StringUtils.LF);
        System.out.println("=================================================" + StringUtils.LF);
        Fields fields = classFile.getFields();

        // 当前字段
        FieldInfo fieldInfo = fields.findByNameAndType(nameAndType);
        if(fieldInfo != null) {
            System.out.println(fieldInfo + StringUtils.LF);
            System.out.println("=================================================" + StringUtils.LF);

            //displayAttributes(fieldInfo.getAttributesList());
        }

        // 其他字段
        System.out.println("Available Fields:");
        for(FieldInfo item : fields.getEntries()) {
            System.out.println(item);
        }
    }

    public static void displayMethod(ClassFile classFile, String nameAndType) {
        MethodStandardVisitor visitor = new MethodStandardVisitor(nameAndType);
        classFile.accept(visitor);
//        System.out.println("=================================================" + StringUtils.LF);
//        System.out.println(classFile.getConstantPool() + StringUtils.LF);
//        System.out.println("=================================================" + StringUtils.LF);
//        Methods methods = classFile.getMethods();
//
//        // 当前方法
//        MethodInfo methodInfo = methods.findByNameAndType(nameAndType);
//        if(methodInfo != null) {
//            System.out.println(methodInfo + StringUtils.LF);
//            System.out.println("=================================================" + StringUtils.LF);
//
//            displayAttributes(methodInfo.getAttributesList());
//        }
//
//        // 其他方法
//        System.out.println("Available Methods:");
//        for(MethodInfo item : methods.getEntries()) {
//            System.out.println(item);
//        }
    }

    public static void displayClassFileAttribute(ClassFile classFile) {
        System.out.println("=================================================" + StringUtils.LF);
        System.out.println(classFile.getConstantPool() + StringUtils.LF);
        System.out.println("=================================================" + StringUtils.LF);
        Attributes attributes = classFile.getAttributes();
        // FIXME: 这里要修改
        //List<AttributeInfo> attributeList = attributes.getEntries();
        //displayAttributes(attributeList);
    }

    public static void displayAttributes(List<AttributeInfo> list) {
        if(list == null || list.size() < 1) return;

        for(AttributeInfo item : list) {
            System.out.println(item);
        }
        System.out.println("=================================================" + StringUtils.LF);
    }
    // endregion
}
