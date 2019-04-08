package lsieun.asm;

public interface Opcodes {
    // ASM API versions.

    int ASM4 = 4 << 16 | 0 << 8;
    int ASM5 = 5 << 16 | 0 << 8;
    int ASM6 = 6 << 16 | 0 << 8;
    int ASM7 = 7 << 16 | 0 << 8;

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(Opcodes.ASM4));
        System.out.println(Integer.toBinaryString(Opcodes.ASM5));
        System.out.println(Integer.toBinaryString(Opcodes.ASM6));
        System.out.println(Integer.toBinaryString(Opcodes.ASM7));
        System.out.println(Opcodes.ASM4);
        System.out.println(Opcodes.ASM5);
        System.out.println(Opcodes.ASM6);
        System.out.println(Opcodes.ASM7);
    }
}
