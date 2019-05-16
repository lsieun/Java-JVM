package lsieun.sample;

import lsieun.bytecode.generic.cst.OpcodeConst;

public class HelloWorld {
    public void testSimple() {
        boolean flag = true;
        int i;
        if (flag) {
            i = 1;
        } else {
            i = 2;
        }
        //System.out.println(100);
        //System.out.printf("%s\n", "World", "Hello");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 256; i++) {
            String opcodeName = OpcodeConst.getOpcodeName(i);
            System.out.println("- " + opcodeName);
        }
    }
}
