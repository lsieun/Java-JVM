package lsieun.asm.example.e;

import java.util.Arrays;

import org.objectweb.asm.Type;

public class TypeUtilTest {
    public static void main(String[] args) {
        Type intType = Type.INT_TYPE;
        System.out.println(intType.getInternalName());
        System.out.println(intType.getDescriptor());

        Type stringType = Type.getType(String.class);
        System.out.println(stringType.getInternalName());
        System.out.println(stringType.getDescriptor());

        Type objectType = Type.getType(Object.class);
        System.out.println(objectType.getInternalName());
        System.out.println(objectType.getDescriptor());

        Type[] argumentTypes = Type.getArgumentTypes("(IJLjava/lang/String;)V");
        System.out.println(Arrays.toString(argumentTypes));
        Type returnType = Type.getReturnType("(I)V");
        System.out.println(returnType);
    }
}
