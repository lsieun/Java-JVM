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
        for (int i = 0; i < 256; i++) {
            String opcodeName = OpcodeConst.getOpcodeName(i);
            System.out.println("- " + opcodeName);
        }
    }
}
