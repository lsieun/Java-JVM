package lsieun.bytecode.generic.type;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.bytecode.generic.Utility;
import lsieun.bytecode.generic.cst.TypeConst;

/**
 * Abstract super class for all possible java types, namely cst types
 * such as int, object types like String and array types, e.g. int[]<br/><br/>
 *
 * <p>
 * 我对Type的理解是这样的：
 * </p>
 * <p>
 * （1）Java language有自己的类型，例如int/float/Object
 * </p>
 * <p>
 * （2）ClassFile对于类型有自己内部的表示，例如I/F/Ljava/lang/Object;
 * </p>
 * <p>
 * （3）Type类，虽然自己是一个表达“数据类型”的类，但这并不是它的最终目的，
 * 它的目的是将Java language和ClassFile的数据类型进行“中间”连接。
 * 举一个例子：先将美元兑换成银子，再用银子兑换成欧元，那么银子就起到“中间”连接的作用。
 * </p>
 */
public abstract class Type {
    private final byte type;

    private String signature; // signature for the type

    protected Type(final byte t, final String s) {
        type = t;
        signature = s;
    }

    /**
     * @return signature for given type.
     */
    public String getSignature() {
        return signature;
    }


    /**
     * @return type as defined in Constants
     */
    public byte getType() {
        return type;
    }

    /*
     * Currently only used by the ArrayType constructor.
     * The signature has a complicated dependency on other parameter
     * so it's tricky to do it in a call to the super ctor.
     */
    void setSignature(final String signature) {
        this.signature = signature;
    }

    /**
     * @return stack size of this type (2 for long and double, 0 for void, 1 otherwise)
     */
    public int getSize() {
        switch (type) {
            case TypeConst.T_DOUBLE:
            case TypeConst.T_LONG:
                return 2;
            case TypeConst.T_VOID:
                return 0;
            default:
                return 1;
        }
    }

    /**
     * @return Type string, e.g. `int[]'
     */
    @Override
    public String toString() {
        return ((this.equals(Type.NULL) || (type >= TypeConst.T_UNKNOWN))) ? signature : Utility
                .signatureToString(signature, false);
    }

    /**
     * Predefined constants
     */
    public static final BasicType VOID = new BasicType(TypeConst.T_VOID);
    public static final BasicType BOOLEAN = new BasicType(TypeConst.T_BOOLEAN);
    public static final BasicType INT = new BasicType(TypeConst.T_INT);
    public static final BasicType SHORT = new BasicType(TypeConst.T_SHORT);
    public static final BasicType BYTE = new BasicType(TypeConst.T_BYTE);
    public static final BasicType LONG = new BasicType(TypeConst.T_LONG);
    public static final BasicType DOUBLE = new BasicType(TypeConst.T_DOUBLE);
    public static final BasicType FLOAT = new BasicType(TypeConst.T_FLOAT);
    public static final BasicType CHAR = new BasicType(TypeConst.T_CHAR);
    public static final ObjectType OBJECT = new ObjectType("java.lang.Object");
    public static final ObjectType CLASS = new ObjectType("java.lang.Class");
    public static final ObjectType STRING = new ObjectType("java.lang.String");
    public static final ObjectType STRINGBUFFER = new ObjectType("java.lang.StringBuffer");
    public static final ObjectType THROWABLE = new ObjectType("java.lang.Throwable");
    public static final Type[] NO_ARGS = new Type[0]; // EMPTY, so immutable
    public static final ReferenceType NULL = new ReferenceType() {
    };
    public static final Type UNKNOWN = new Type(TypeConst.T_UNKNOWN, "<unknown object>") {
    };

    private static final ThreadLocal<Integer> consumed_chars = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return Integer.valueOf(0);
        }
    };//int consumed_chars=0; // Remember position in string, see getArgumentTypes

    // region private static
    private static int unwrap(final ThreadLocal<Integer> tl) {
        return tl.get().intValue();
    }


    private static void wrap(final ThreadLocal<Integer> tl, final int value) {
        tl.set(Integer.valueOf(value));
    }
    // endregion

    // region Type-->ClassFile

    /**
     * Convert type to Java method signature, e.g. int[] f(java.lang.String x)
     * becomes (Ljava/lang/String;)[I
     *
     * @param return_type what the method returns
     * @param arg_types   what are the argument types
     * @return method signature for given type(s).
     */
    public static String getMethodSignature(final Type return_type, final Type[] arg_types) {
        final StringBuilder buf = new StringBuilder("(");
        if (arg_types != null) {
            for (final Type arg_type : arg_types) {
                buf.append(arg_type.getSignature());
            }
        }
        buf.append(')');
        buf.append(return_type.getSignature());
        return buf.toString();
    }
    // endregion

    // region ClassFile-->Type

    /**
     * Convert signature to a Type object.
     *
     * @param signature signature string such as Ljava/lang/String;
     * @return type object
     */
    public static Type getType(final String signature) throws StringIndexOutOfBoundsException {
        final byte type = Utility.typeOfSignature(signature);
        if (type <= TypeConst.T_VOID) {
            //corrected concurrent private static field acess
            wrap(consumed_chars, 1);
            return BasicType.getType(type);
        } else if (type == TypeConst.T_ARRAY) {
            int dim = 0;
            do { // Count dimensions
                dim++;
            } while (signature.charAt(dim) == '[');
            // Recurse, but just once, if the signature is ok
            final Type t = getType(signature.substring(dim));
            //corrected concurrent private static field acess
            //  consumed_chars += dim; // update counter - is replaced by
            final int _temp = unwrap(consumed_chars) + dim;
            wrap(consumed_chars, _temp);
            return new ArrayType(t, dim);
        } else { // type == T_REFERENCE
            // Utility.signatureToString understands how to parse
            // generic types.
            final String parsedSignature = Utility.signatureToString(signature, false);
            wrap(consumed_chars, parsedSignature.length() + 2); // "Lblabla;" `L' and `;' are removed
            return ObjectType.getInstance(parsedSignature.replace('/', '.'));
        }
    }

    /**
     * Convert return value of a method (signature) to a Type object.
     *
     * @param signature signature string such as (Ljava/lang/String;)V
     * @return return type
     */
    public static Type getReturnType(final String signature) {
        try {
            // Read return type after `)'
            final int index = signature.lastIndexOf(')') + 1;
            return getType(signature.substring(index));
        } catch (final StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassFormatException("Invalid method signature: " + signature, e);
        }
    }

    /**
     * Convert arguments of a method (signature) to an array of Type objects.
     *
     * @param signature signature string such as (Ljava/lang/String;)V
     * @return array of argument types
     */
    public static Type[] getArgumentTypes(final String signature) {
        final List<Type> vec = new ArrayList<>();
        int index;
        Type[] types;
        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            index = 1; // current string position
            while (signature.charAt(index) != ')') {
                vec.add(getType(signature.substring(index)));
                //corrected concurrent private static field acess
                index += unwrap(consumed_chars); // update position
            }
        } catch (final StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassFormatException("Invalid method signature: " + signature, e);
        }
        types = new Type[vec.size()];
        vec.toArray(types);
        return types;
    }

    // endregion


    // region ClassFile

    public static int getReturnTypeSize(final String methodSignature) {
        final int index = methodSignature.lastIndexOf(')') + 1;
        String returnSignature = methodSignature.substring(index);
        int sizeAndCharNum = getTypeSizeAndCharNum(returnSignature);
        return size(sizeAndCharNum);
    }

    public static int getArgumentTypesSize(final String signature) {
        int res = 0;
        int index;
        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            index = 1; // current string position
            while (signature.charAt(index) != ')') {
                final int coded = getTypeSizeAndCharNum(signature.substring(index));
                res += size(coded);
                index += consumed(coded);
            }
        } catch (final StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassFormatException("Invalid method signature: " + signature, e);
        }
        return res;
    }


    /**
     * GOOD_CODE: 这个“方法的实现思路”很不错，它将“两个int值”合并成“一个int值”进行返回。
     * <br/><br/>
     * <p>
     * 这个方法的返回值（return value），其实包含了两个部分数据：
     * </p><br/>
     * <p>
     * 第一部分数据，就是这个Type究竟占用几个slot，它的取值只能是1或2。
     * 例如，对于int、float、Object，它所占用的slot的大小就是1个；
     * 而对于long、double，它所占用的slot大小就是2个。
     * </p><br/>
     * <p>
     * 第二部分数据，就是这个Signature的字符长度。
     * 比如说，I、F、D、J，它的长度是1；对于Ljava/lang/String;，它的长度是18。
     * </p><br/>
     * <p>
     * 这个方法精妙的地方在于：第一部分数据的取值范围就是1和2,那么这部分只占2个bit就可以进行存储了，
     * 第二部分的数据在进行存储的时候，只要向左移动2个bit就可以了。
     * </p>
     *
     * @param signature
     * @return
     * @throws StringIndexOutOfBoundsException
     */
    public static int getTypeSizeAndCharNum(final String signature) throws StringIndexOutOfBoundsException {
        final byte type = Utility.typeOfSignature(signature);
        if (type <= TypeConst.T_VOID) {
            return encode(BasicType.getType(type).getSize(), 1);
        } else if (type == TypeConst.T_ARRAY) {
            int dim = 0;
            do { // Count dimensions
                dim++;
            } while (signature.charAt(dim) == '[');
            // Recurse, but just once, if the signature is ok
            final int consumed = consumed(getTypeSizeAndCharNum(signature.substring(dim)));
            return encode(1, dim + consumed);
        } else { // type == T_REFERENCE
            final int index = signature.indexOf(';'); // Look for closing `;'
            if (index < 0) {
                throw new ClassFormatException("Invalid signature: " + signature);
            }
            return encode(1, index + 1);
        }
    }
    // endregion

    // region auxiliary methods

    public static int size(final int coded) {
        return coded & 3;
    }

    static int consumed(final int coded) {
        return coded >> 2;
    }

    static int encode(final int size, final int consumed) {
        return consumed << 2 | size;
    }

    // endregion

    /**
     * Convert runtime java.lang.Class to BCEL Type object.
     *
     * @param cl Java class
     * @return corresponding Type object
     */
    public static Type getType(final java.lang.Class<?> cl) {
        if (cl == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        /* That's an amzingly easy case, because getName() returns
         * the signature. That's what we would have liked anyway.
         */
        if (cl.isArray()) {
            return getType(cl.getName());
        } else if (cl.isPrimitive()) {
            if (cl == Integer.TYPE) {
                return INT;
            } else if (cl == Void.TYPE) {
                return VOID;
            } else if (cl == Double.TYPE) {
                return DOUBLE;
            } else if (cl == Float.TYPE) {
                return FLOAT;
            } else if (cl == Boolean.TYPE) {
                return BOOLEAN;
            } else if (cl == Byte.TYPE) {
                return BYTE;
            } else if (cl == Short.TYPE) {
                return SHORT;
            } else if (cl == Byte.TYPE) {
                return BYTE;
            } else if (cl == Long.TYPE) {
                return LONG;
            } else if (cl == Character.TYPE) {
                return CHAR;
            } else {
                throw new IllegalStateException("Ooops, what primitive type is " + cl);
            }
        } else { // "Real" class
            return ObjectType.getInstance(cl.getName());
        }
    }
}
