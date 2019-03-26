package lsieun.bytecode.example;

import java.io.FileNotFoundException;
import java.io.Serializable;

public final class HelloWorld implements Serializable, Cloneable {
    private static final boolean boolTrueValue = true;
    private final boolean boolFalseValue = false;
    private static final byte byteValue = 16;
    private static final short shortValue = 32;
    private static final char charValue = 'A';
    private static final int intValue = 512;
    private static final long longValue = 1024L;
    private static final float floatValue = 3.14F;
    private static final double doubleValue = 6.28D;
    public String strValue = "靡不有初，鲜克有终";
    private int[] intArray;
    private static final Object[] objectArray = new Object[0];

    public final void testMethod(int firstParameter, String secondParameter) throws FileNotFoundException {
        int i = 10;
        int a = i + 2;
        System.out.println("Hi");
        try{
            int x = 10 / 0;
        }
        catch (ArithmeticException ex) {
            ex.printStackTrace();
        }

        return;
    }

    public <T> void testGeneric(T type) {
        //
    }

    public static class InnerHelloClass {
        //
    }

    public static final class InnerWorldClass {
        //
    }

    public static void main(String[] args) {
        System.out.println(HelloWorld.intValue);
    }
}
