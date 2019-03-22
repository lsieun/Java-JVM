package lsieun.bytecode;

import lsieun.bytecode.classfile.JavaClass;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.classfile.ClassParser;
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

        ClassParser classParser = new ClassParser(byteDashboard);
        JavaClass javaClass = classParser.parse();
        System.out.println(javaClass);
    }
}
