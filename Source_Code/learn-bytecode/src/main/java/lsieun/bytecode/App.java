package lsieun.bytecode;

import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.JavaClass;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.classfile.UtilityClassParser;
import lsieun.utils.StringUtils;
import lsieun.utils.io.FileUtils;
import lsieun.utils.io.JarUtils;

public class App {
    private static final boolean READ_JAR = false;

    public static void main(String[] args) {
        String url = null;
        byte[] bytes = null;

        if (READ_JAR) {
            String jarPath = "/usr/local/jdk8/jre/lib/rt.jar";
            String entryName = "java/lang/Object.class";
            url = "jar:file:" + jarPath + "!/" + entryName;
            bytes = JarUtils.readBytes(jarPath, entryName);
        } else {
            String dir = App.class.getResource(".").getPath();
            String filepath = dir + "example/HelloWorld.class";
            url = "file://" + filepath;
            bytes = FileUtils.readBytes(filepath);
        }

        ByteDashboard byteDashboard = new ByteDashboard(url, bytes);
        System.out.println(byteDashboard);

        UtilityClassParser classParser = new UtilityClassParser(byteDashboard);
        JavaClass javaClass = classParser.parse();

        // 打印ClassFile
        //displayClassFile(javaClass);

        displayMethodAttribute(javaClass, "testMethod:(ILjava/lang/String;)V");
    }

    public static void displayClassFile(JavaClass javaClass) {
        System.out.println(javaClass);
    }

    public static void displayMethodAttribute(JavaClass javaClass, String nameAndType) {
        System.out.println(javaClass.getConstantPool() + StringUtils.LF);
        Methods methods = javaClass.getMethods();

        MethodInfo methodInfo = methods.findByNameAndType(nameAndType);
        if(methodInfo == null) return;
        System.out.println(methodInfo + StringUtils.LF);

        AttributeInfo codeAttr = methodInfo.findAttribute("Code");
        System.out.println(codeAttr);
    }
}
