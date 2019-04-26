package lsieun.z;

import static lsieun.asm.Opcodes.ACC_PRIVATE;
import static lsieun.asm.Opcodes.ACC_PUBLIC;
import static lsieun.asm.Opcodes.ACC_SUPER;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lsieun.App;
import lsieun.asm.ClassWriter;
import lsieun.asm.Opcodes;

public class App02 {
    public static void main(String[] args) {
        ClassWriter cw = new ClassWriter();
        cw.visit(Opcodes.V1_8, ACC_PUBLIC+ACC_SUPER,
                "lsieun/sample/ABC", null,
                "java/lang/Object", null);
        cw.visitSource("ABC.java", null);
        cw.visitField(ACC_PRIVATE, "intValue", "I", null, null).visitEnd();
        cw.visitField(ACC_PRIVATE, "floatValue", "F", null, null).visitEnd();
        cw.visitField(ACC_PRIVATE, "strValue", "Ljava/lang/String;", null, null).visitEnd();
        cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null).visitEnd();
        cw.visitMethod(ACC_PUBLIC, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();

        writeBytes(bytes, "lsieun.sample.ABC");

        MyClassLoader myClassLoader = new MyClassLoader();
        Class c = myClassLoader.defineClass("lsieun.sample.ABC", bytes);
        System.out.println(c.getName());
    }

    public static void writeBytes(byte[] bytes, String className) {
        String dir = App.class.getResource("/").getPath();
        String filepath = dir + className.replace('.', File.separatorChar) + ".class";

        OutputStream out = null;

        try {
            out = new FileOutputStream(filepath);
            out = new BufferedOutputStream(out);
            out.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }
}
