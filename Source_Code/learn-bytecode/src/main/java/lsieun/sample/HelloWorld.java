package lsieun.sample;

import java.io.Serializable;

public class HelloWorld implements Serializable, Cloneable {
    private static final int intValue = 1;
    private float floatValue;
    private long longValue;
    private static final double doubleValue = 3.1415926;

    public static void testSimple() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String format = "Username='%s' Password='%s'";
        String firstLine = String.format(format, "lsieun", "123456");
        System.out.println(firstLine);
    }

    private void testPrivate() {
        System.out.println("private method");
    }
}
