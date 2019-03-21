package lsieun.bcel;


import java.io.File;
import java.io.IOException;

import org.junit.Test;

import lsieun.bcel.classfile.JavaClass;

public class ClassParserTest {
    @Test
    public void parse() throws IOException {
        String path = System.getProperty("user.dir");
        String filename = path + File.separator + "target/classes/lsieun/bcel/ClassParser.class";
        System.out.println("file://" + filename);
        ClassParser parser = new ClassParser(filename);
        JavaClass clazz = parser.parse();
        System.out.println(clazz);
    }
}
