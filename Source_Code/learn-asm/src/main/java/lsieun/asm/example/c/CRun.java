package lsieun.asm.example.c;

import static org.objectweb.asm.Opcodes.ASM4;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class CRun {
    public static void main(String[] args) {
        byte[] b1 = null;
        ClassReader cr = new ClassReader(b1);
        ClassWriter cw = new ClassWriter(cr, 0);
        // cv forwards all events to cw
        ClassVisitor cv = new ChangeVersionAdapter(ASM4, cw);

        cr.accept(cv, 0);
        byte[] b2 = cw.toByteArray();
    }
}
