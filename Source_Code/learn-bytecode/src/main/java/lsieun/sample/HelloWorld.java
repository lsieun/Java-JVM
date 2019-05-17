package lsieun.sample;

import java.util.HashMap;
import java.util.Map;

import lsieun.bytecode.generic.cst.OpcodeConst;

public class HelloWorld {
    public void testSimple() {
        String[] array = new String[2];
        array[0] = array[1] = "Hello";
    }

    public static void main(String[] args) {
        for (int i = 0; i < 256; i++) {
            String opcodeName = OpcodeConst.getOpcodeName(i);
            System.out.println("- " + opcodeName);
        }
    }
}
