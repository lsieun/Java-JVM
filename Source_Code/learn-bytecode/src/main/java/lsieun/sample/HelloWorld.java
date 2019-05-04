package lsieun.sample;

import java.io.Serializable;

public class HelloWorld implements Serializable, Cloneable {
    private static final int intValue = 1;
    private float floatValue;
    private long longValue;

    public static void testSimple() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void testPrivate() {
        System.out.println("private method");
    }
}
