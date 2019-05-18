package lsieun.sample;

import java.util.HashMap;
import java.util.Map;

import lsieun.bytecode.generic.cst.OpcodeConst;

public class HelloWorld {
    public void testSimple() {
        int value = 1;
        int result = 0;

        switch (value) {
            case 1:
                result = 1;
                break;
            case 2:
                result = 2;
                break;
            case 3:
                result = 3;
                break;
            default:
                result = 4;
        }
    }

    public static void main(String[] args) {
        String str = "abcdef";
        int hashCode = str.hashCode();
        System.out.println(hashCode);
        int sum = 0;
        for(int i=0; i<str.length(); i++) {
            sum = sum * 31 + str.charAt(i);
        }
        System.out.println(sum);
        double sqrt = Math.sqrt(Short.MAX_VALUE);
        System.out.println(sqrt);
    }
}
