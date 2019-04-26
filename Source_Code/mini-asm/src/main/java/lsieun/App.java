package lsieun;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import lsieun.asm.ClassReader;

public class App {
    /** The size of the temporary byte array used to read class input streams chunk by chunk. */
    private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;

    public static void main(String[] args) throws IOException {
        //ClassReader cr = ClassReader.getClassReader("java.lang.Object");
        InputStream in = getInputStream("lsieun.sample.HelloWorld");
        byte[] bytes = readStream(in, true);
        ClassReader cr = new ClassReader(bytes);
        String className = cr.getClassName();
        System.out.println(className);
        String[] interfaces = cr.getInterfaces();
        System.out.println(Arrays.toString(interfaces));
    }



    public static byte[] readStream(final InputStream inputStream, final boolean close) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[INPUT_STREAM_DATA_CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, bytesRead);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } finally {
            if (close) {
                inputStream.close();
            }
        }
    }

    public static InputStream getInputStream(String className) throws FileNotFoundException {
        String dir = App.class.getResource("/").getPath();
        String filepath = dir + className.replace('.', File.separatorChar) + ".class";

        InputStream in = new FileInputStream(filepath);
        return in;
    }

    public static InputStream getInputStream2(String className) {
        InputStream in = ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class");
        return in;
    }
}
