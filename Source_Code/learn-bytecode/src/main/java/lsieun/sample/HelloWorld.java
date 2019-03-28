package lsieun.sample;

import static lsieun.bytecode.classfile.basic.TypeConst.T_BYTE;
import static lsieun.bytecode.classfile.basic.TypeConst.T_INT;
import static lsieun.bytecode.classfile.basic.TypeConst.T_SHORT;

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

    @Deprecated
    public void testSimple() {
        //int i = 1;

//        int a = 10;
//        int b = 20;
//        int c = a + b;
//        System.out.println(c);

//        long a = 65537L;
//        long b = 2048000L;
//        long c = a + b;

//        int a = 1;
//        int b = 2;
//        int c = 3;
//        int d = 4;
//        int e = 5;
//        int f = 6;
//        int sum = e + f;
        String str = "Hello";

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

    private static final short[][] TYPE_OF_OPERANDS = {
            // region type of operand
            {}/*nop*/, {}/*aconst_null*/, {}/*iconst_m1*/, {}/*iconst_0*/,
            {}/*iconst_1*/, {}/*iconst_2*/, {}/*iconst_3*/, {}/*iconst_4*/,
            {}/*iconst_5*/, {}/*lconst_0*/, {}/*lconst_1*/, {}/*fconst_0*/,
            {}/*fconst_1*/, {}/*fconst_2*/, {}/*dconst_0*/, {}/*dconst_1*/,
            {T_BYTE}/*bipush*/, {T_SHORT}/*sipush*/, {T_BYTE}/*ldc*/,
            {T_SHORT}/*ldc_w*/, {T_SHORT}/*ldc2_w*/,
            {T_BYTE}/*iload*/, {T_BYTE}/*lload*/, {T_BYTE}/*fload*/,
            {T_BYTE}/*dload*/, {T_BYTE}/*aload*/, {}/*iload_0*/,
            {}/*iload_1*/, {}/*iload_2*/, {}/*iload_3*/, {}/*lload_0*/,
            {}/*lload_1*/, {}/*lload_2*/, {}/*lload_3*/, {}/*fload_0*/,
            {}/*fload_1*/, {}/*fload_2*/, {}/*fload_3*/, {}/*dload_0*/,
            {}/*dload_1*/, {}/*dload_2*/, {}/*dload_3*/, {}/*aload_0*/,
            {}/*aload_1*/, {}/*aload_2*/, {}/*aload_3*/, {}/*iaload*/,
            {}/*laload*/, {}/*faload*/, {}/*daload*/, {}/*aaload*/,
            {}/*baload*/, {}/*caload*/, {}/*saload*/, {T_BYTE}/*istore*/,
            {T_BYTE}/*lstore*/, {T_BYTE}/*fstore*/, {T_BYTE}/*dstore*/,
            {T_BYTE}/*astore*/, {}/*istore_0*/, {}/*istore_1*/,
            {}/*istore_2*/, {}/*istore_3*/, {}/*lstore_0*/, {}/*lstore_1*/,
            {}/*lstore_2*/, {}/*lstore_3*/, {}/*fstore_0*/, {}/*fstore_1*/,
            {}/*fstore_2*/, {}/*fstore_3*/, {}/*dstore_0*/, {}/*dstore_1*/,
            {}/*dstore_2*/, {}/*dstore_3*/, {}/*astore_0*/, {}/*astore_1*/,
            {}/*astore_2*/, {}/*astore_3*/, {}/*iastore*/, {}/*lastore*/,
            {}/*fastore*/, {}/*dastore*/, {}/*aastore*/, {}/*bastore*/,
            {}/*castore*/, {}/*sastore*/, {}/*pop*/, {}/*pop2*/, {}/*dup*/,
            {}/*dup_x1*/, {}/*dup_x2*/, {}/*dup2*/, {}/*dup2_x1*/,
            {}/*dup2_x2*/, {}/*swap*/, {}/*iadd*/, {}/*ladd*/, {}/*fadd*/,
            {}/*dadd*/, {}/*isub*/, {}/*lsub*/, {}/*fsub*/, {}/*dsub*/,
            {}/*imul*/, {}/*lmul*/, {}/*fmul*/, {}/*dmul*/, {}/*idiv*/,
            {}/*ldiv*/, {}/*fdiv*/, {}/*ddiv*/, {}/*irem*/, {}/*lrem*/,
            {}/*frem*/, {}/*drem*/, {}/*ineg*/, {}/*lneg*/, {}/*fneg*/,
            {}/*dneg*/, {}/*ishl*/, {}/*lshl*/, {}/*ishr*/, {}/*lshr*/,
            {}/*iushr*/, {}/*lushr*/, {}/*iand*/, {}/*land*/, {}/*ior*/,
            {}/*lor*/, {}/*ixor*/, {}/*lxor*/, {T_BYTE, T_BYTE}/*iinc*/,
            {}/*i2l*/, {}/*i2f*/, {}/*i2d*/, {}/*l2i*/, {}/*l2f*/, {}/*l2d*/,
            {}/*f2i*/, {}/*f2l*/, {}/*f2d*/, {}/*d2i*/, {}/*d2l*/, {}/*d2f*/,
            {}/*i2b*/, {}/*i2c*/, {}/*i2s*/, {}/*lcmp*/, {}/*fcmpl*/,
            {}/*fcmpg*/, {}/*dcmpl*/, {}/*dcmpg*/, {T_SHORT}/*ifeq*/,
            {T_SHORT}/*ifne*/, {T_SHORT}/*iflt*/, {T_SHORT}/*ifge*/,
            {T_SHORT}/*ifgt*/, {T_SHORT}/*ifle*/, {T_SHORT}/*if_icmpeq*/,
            {T_SHORT}/*if_icmpne*/, {T_SHORT}/*if_icmplt*/,
            {T_SHORT}/*if_icmpge*/, {T_SHORT}/*if_icmpgt*/,
            {T_SHORT}/*if_icmple*/, {T_SHORT}/*if_acmpeq*/,
            {T_SHORT}/*if_acmpne*/, {T_SHORT}/*goto*/, {T_SHORT}/*jsr*/,
            {T_BYTE}/*ret*/, {}/*tableswitch*/, {}/*lookupswitch*/,
            {}/*ireturn*/, {}/*lreturn*/, {}/*freturn*/, {}/*dreturn*/,
            {}/*areturn*/, {}/*return*/, {T_SHORT}/*getstatic*/,
            {T_SHORT}/*putstatic*/, {T_SHORT}/*getfield*/,
            {T_SHORT}/*putfield*/, {T_SHORT}/*invokevirtual*/,
            {T_SHORT}/*invokespecial*/, {T_SHORT}/*invokestatic*/,
            {T_SHORT, T_BYTE, T_BYTE}/*invokeinterface*/, {T_SHORT, T_BYTE, T_BYTE}/*invokedynamic*/,
            {T_SHORT}/*new*/, {T_BYTE}/*newarray*/,
            {T_SHORT}/*anewarray*/, {}/*arraylength*/, {}/*athrow*/,
            {T_SHORT}/*checkcast*/, {T_SHORT}/*instanceof*/,
            {}/*monitorenter*/, {}/*monitorexit*/, {T_BYTE}/*wide*/,
            {T_SHORT, T_BYTE}/*multianewarray*/, {T_SHORT}/*ifnull*/,
            {T_SHORT}/*ifnonnull*/, {T_INT}/*goto_w*/, {T_INT}/*jsr_w*/,
            {}/*breakpoint*/, {}, {}, {}, {}, {}, {}, {},
            {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
            {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
            {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
            {}/*impdep1*/, {}/*impdep2*/
            // endregion
    };

    public static void main(String[] args) {
        System.out.println(HelloWorld.intValue);
    }
}
