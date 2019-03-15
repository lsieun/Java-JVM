package lsieun.bcel.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
    public static void closeQuitely(Closeable c) {
        if(c != null) {
            try {
                c.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}
