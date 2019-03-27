package lsieun.bytecode.example;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

import lsieun.bytecode.exceptions.ClassFormatException;

public final class HelloWorld<K> implements Serializable, Cloneable, Comparable<HelloWorld> {
    private static final boolean boolTrueValue = true;
    private final boolean boolFalseValue = false;
    private static final byte byteValue = 16;
    private static final short shortValue = 32;
    private static final char charValue = 'A';
    private static final int intValue = 512;
    private static final long longValue = 1024L;
    private static final float floatValue = 3.14F;
    private static final double doubleValue = 6.28D;
    // object
    public String strValue = "靡不有初，鲜克有终";
    public Object objValue;
    // array
    private int[] intArray;
    private static final Object[] objectArray = new Object[0];
    // generic
    private K genericField;

    public void testSimple() {
        int i = 1;
    }

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
        catch (ClassFormatException cfe) {
            cfe.printStackTrace();
        }

        return;
    }

    public <K> void testGeneric(K type, int i, String str) throws ClassFormatException, FileNotFoundException {
        //
    }

    @Override
    public int compareTo(HelloWorld o) {
        return 0;
    }

    public static <T> String list2str(List<T> list, String separator) {
        if(list == null || list.size() < 1) return "";

        StringBuilder sb = new StringBuilder();

        int size = list.size();
        for(int i=0; i<size-1; i++) {
            T item = list.get(i);
            sb.append(item.toString() + separator);
        }
        String theLast = list.get(size-1).toString();
        sb.append(theLast);

        return sb.toString();
    }

    public static <T> String list2str(List<T> list, String start, String stop, String separator) {
        if(list == null || list.size() < 1) return null;

        StringBuilder sb = new StringBuilder();
        sb.append(start);

        String content = list2str(list, separator);
        sb.append(content);

        sb.append(stop);
        return sb.toString();
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
