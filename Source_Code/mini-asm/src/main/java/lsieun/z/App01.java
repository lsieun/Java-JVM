package lsieun.z;

import java.io.InputStream;

import lsieun.App;
import lsieun.asm.ClassReader;

public class App01 {
    public static void main(String[] args) throws Exception {
        InputStream in = App.getInputStream("lsieun.sample.HelloWorld");
        byte[] bytes = App.readStream(in, true);
        ClassReader cr = new ClassReader(bytes);

        ClassPrinter cp = new ClassPrinter();
        cr.accept(cp);
    }
}
