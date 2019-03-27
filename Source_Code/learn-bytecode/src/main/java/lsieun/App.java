package lsieun;

import java.util.List;

import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
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

        ByteDashboard byteDashboard = new ByteDashboard(url, bytes);
        //System.out.println(byteDashboard);

        ClassParser classParser = new ClassParser(byteDashboard);
        ClassFile classFile = classParser.parse();



        // 打印ClassFile
        String target = PropertyUtils.getProperty("classfile.content.target");
        if("ClassFile".equalsIgnoreCase(target)) {
            displayClassFile(classFile);
        }
        else if("Field".equalsIgnoreCase(target)) {
            String fieldSignature = PropertyUtils.getProperty("classfile.content.field.signature");
            displayFieldAttribute(classFile, fieldSignature);
        }
        else if("Method".equalsIgnoreCase(target)) {
            String methodSignature = PropertyUtils.getProperty("classfile.content.method.signature");
            displayMethodAttribute(classFile, methodSignature);
        }
        else if("Attribute".equalsIgnoreCase(target)) {
            displayClassFileAttribute(classFile);
        }
        else {
            System.out.println("");
        }


        //displayMethodAttribute(classFile, "testMethod:(ILjava/lang/String;)V");
    }

    public static void displayClassFile(ClassFile classFile) {
        System.out.println(classFile);
    }

    public static void displayFieldAttribute(ClassFile classFile, String nameAndType) {
        System.out.println("=================================================" + StringUtils.LF);
        System.out.println(classFile.getConstantPool() + StringUtils.LF);
        System.out.println("=================================================" + StringUtils.LF);
        Fields fields = classFile.getFields();

        // 当前字段
        FieldInfo fieldInfo = fields.findByNameAndType(nameAndType);
        if(fieldInfo != null) {
            System.out.println(fieldInfo + StringUtils.LF);
            System.out.println("=================================================" + StringUtils.LF);

            displayAttributes(fieldInfo.getAttributesList());
        }

        // 其他字段
        System.out.println("Other Fields:");
        for(FieldInfo item : fields.getFieldList()) {
            System.out.println(item);
        }
    }

    public static void displayMethodAttribute(ClassFile classFile, String nameAndType) {
        System.out.println("=================================================" + StringUtils.LF);
        System.out.println(classFile.getConstantPool() + StringUtils.LF);
        System.out.println("=================================================" + StringUtils.LF);
        Methods methods = classFile.getMethods();

        // 当前方法
        MethodInfo methodInfo = methods.findByNameAndType(nameAndType);
        if(methodInfo != null) {
            System.out.println(methodInfo + StringUtils.LF);
            System.out.println("=================================================" + StringUtils.LF);

            displayAttributes(methodInfo.getAttributesList());
        }

        // 其他方法
        System.out.println("Other Methods:");
        for(MethodInfo item : methods.getMethodList()) {
            System.out.println(item);
        }
    }

    public static void displayClassFileAttribute(ClassFile classFile) {
        System.out.println("=================================================" + StringUtils.LF);
        System.out.println(classFile.getConstantPool() + StringUtils.LF);
        System.out.println("=================================================" + StringUtils.LF);
        Attributes attributes = classFile.getAttributes();
        List<AttributeInfo> attributeList = attributes.getAttributeList();
        displayAttributes(attributeList);
    }

    public static void displayAttributes(List<AttributeInfo> list) {
        if(list == null || list.size() < 1) return;

        for(AttributeInfo item : list) {
            System.out.println(item);
        }
        System.out.println("=================================================" + StringUtils.LF);
    }
}
