package lsieun.asm;

/**
 * A Java field or method type. This class can be used to make it easier to manipulate type and
 * method descriptors.
 */
public final class Type {

    /** The sort of the {@code void} type. See {@link #getSort}. */
    public static final int VOID = 0;

    /** The sort of the {@code boolean} type. See {@link #getSort}. */
    public static final int BOOLEAN = 1;

    /** The sort of the {@code char} type. See {@link #getSort}. */
    public static final int CHAR = 2;

    /** The sort of the {@code byte} type. See {@link #getSort}. */
    public static final int BYTE = 3;

    /** The sort of the {@code short} type. See {@link #getSort}. */
    public static final int SHORT = 4;

    /** The sort of the {@code int} type. See {@link #getSort}. */
    public static final int INT = 5;

    /** The sort of the {@code float} type. See {@link #getSort}. */
    public static final int FLOAT = 6;

    /** The sort of the {@code long} type. See {@link #getSort}. */
    public static final int LONG = 7;

    /** The sort of the {@code double} type. See {@link #getSort}. */
    public static final int DOUBLE = 8;

    /** The sort of array reference types. See {@link #getSort}. */
    public static final int ARRAY = 9;

    /** The sort of object reference types. See {@link #getSort}. */
    public static final int OBJECT = 10;

    /** The sort of method types. See {@link #getSort}. */
    public static final int METHOD = 11;

    /** The (private) sort of object reference types represented with an internal name. */
    private static final int INTERNAL = 12;

    /** The descriptors of the primitive types. */
    private static final String PRIMITIVE_DESCRIPTORS = "VZCBSIFJD";

    /** The {@code void} type. */
    public static final Type VOID_TYPE = new Type(VOID, PRIMITIVE_DESCRIPTORS, VOID, VOID + 1);

    /** The {@code boolean} type. */
    public static final Type BOOLEAN_TYPE = new Type(BOOLEAN, PRIMITIVE_DESCRIPTORS, BOOLEAN, BOOLEAN + 1);

    /** The {@code char} type. */
    public static final Type CHAR_TYPE = new Type(CHAR, PRIMITIVE_DESCRIPTORS, CHAR, CHAR + 1);

    /** The {@code byte} type. */
    public static final Type BYTE_TYPE = new Type(BYTE, PRIMITIVE_DESCRIPTORS, BYTE, BYTE + 1);

    /** The {@code short} type. */
    public static final Type SHORT_TYPE = new Type(SHORT, PRIMITIVE_DESCRIPTORS, SHORT, SHORT + 1);

    /** The {@code int} type. */
    public static final Type INT_TYPE = new Type(INT, PRIMITIVE_DESCRIPTORS, INT, INT + 1);

    /** The {@code float} type. */
    public static final Type FLOAT_TYPE = new Type(FLOAT, PRIMITIVE_DESCRIPTORS, FLOAT, FLOAT + 1);

    /** The {@code long} type. */
    public static final Type LONG_TYPE = new Type(LONG, PRIMITIVE_DESCRIPTORS, LONG, LONG + 1);

    /** The {@code double} type. */
    public static final Type DOUBLE_TYPE = new Type(DOUBLE, PRIMITIVE_DESCRIPTORS, DOUBLE, DOUBLE + 1);

    // -----------------------------------------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------------------------------------

    /**
     * The sort of this type. Either {@link #VOID}, {@link #BOOLEAN}, {@link #CHAR}, {@link #BYTE},
     * {@link #SHORT}, {@link #INT}, {@link #FLOAT}, {@link #LONG}, {@link #DOUBLE}, {@link #ARRAY},
     * {@link #OBJECT}, {@link #METHOD} or {@link #INTERNAL}.
     */
    private final int sort;

    /**
     * A buffer containing the value of this field or method type. This value is an internal name for
     * {@link #OBJECT} and {@link #INTERNAL} types, and a field or method descriptor in the other
     * cases.
     *
     * <p>For {@link #OBJECT} types, this field also contains the descriptor: the characters in
     * [{@link #valueBegin},{@link #valueEnd}) contain the internal name, and those in [{@link
     * #valueBegin} - 1, {@link #valueEnd} + 1) contain the descriptor.
     */
    private final String valueBuffer;

    /**
     * The beginning index, inclusive, of the value of this Java field or method type in {@link
     * #valueBuffer}. This value is an internal name for {@link #OBJECT} and {@link #INTERNAL} types,
     * and a field or method descriptor in the other cases.
     */
    private final int valueBegin;

    /**
     * The end index, exclusive, of the value of this Java field or method type in {@link
     * #valueBuffer}. This value is an internal name for {@link #OBJECT} and {@link #INTERNAL} types,
     * and a field or method descriptor in the other cases.
     */
    private final int valueEnd;

    /**
     * Constructs a reference type.
     *
     * @param sort the sort of this type, see {@link #sort}.
     * @param valueBuffer a buffer containing the value of this field or method type.
     * @param valueBegin the beginning index, inclusive, of the value of this field or method type in
     *     valueBuffer.
     * @param valueEnd the end index, exclusive, of the value of this field or method type in
     *     valueBuffer.
     */
    private Type(final int sort, final String valueBuffer, final int valueBegin, final int valueEnd) {
        this.sort = sort;
        this.valueBuffer = valueBuffer;
        this.valueBegin = valueBegin;
        this.valueEnd = valueEnd;
    }

    // -----------------------------------------------------------------------------------------------
    // Methods to get the sort, dimension, size, and opcodes corresponding to a Type or descriptor.
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns the sort of this type.
     *
     * @return {@link #VOID}, {@link #BOOLEAN}, {@link #CHAR}, {@link #BYTE}, {@link #SHORT}, {@link
     *     #INT}, {@link #FLOAT}, {@link #LONG}, {@link #DOUBLE}, {@link #ARRAY}, {@link #OBJECT} or
     *     {@link #METHOD}.
     */
    public int getSort() {
        return sort == INTERNAL ? OBJECT : sort;
    }

    /**
     * Returns the internal name of the class corresponding to this object or array type. The internal
     * name of a class is its fully qualified name (as returned by Class.getName(), where '.' are
     * replaced by '/'). This method should only be used for an object or array type.
     *
     * @return the internal name of the class corresponding to this object type.
     */
    public String getInternalName() {
        return valueBuffer.substring(valueBegin, valueEnd);
    }

    /**
     * Computes the size of the arguments and of the return value of a method.
     *
     * @param methodDescriptor a method descriptor.
     * @return the size of the arguments of the method (plus one for the implicit this argument),
     *     argumentsSize, and the size of its return value, returnSize, packed into a single int i =
     *     {@code (argumentsSize &lt;&lt; 2) | returnSize} (argumentsSize is therefore equal to {@code
     *     i &gt;&gt; 2}, and returnSize to {@code i &amp; 0x03}).
     */
    public static int getArgumentsAndReturnSizes(final String methodDescriptor) {
        int argumentsSize = 1;
        // Skip the first character, which is always a '('.
        int currentOffset = 1;
        int currentChar = methodDescriptor.charAt(currentOffset);
        // Parse the argument types and compute their size, one at a each loop iteration.
        while (currentChar != ')') {
            if (currentChar == 'J' || currentChar == 'D') {
                currentOffset++;
                argumentsSize += 2;
            } else {
                while (methodDescriptor.charAt(currentOffset) == '[') {
                    currentOffset++;
                }
                if (methodDescriptor.charAt(currentOffset++) == 'L') {
                    // Skip the argument descriptor content.
                    currentOffset = methodDescriptor.indexOf(';', currentOffset) + 1;
                }
                argumentsSize += 1;
            }
            currentChar = methodDescriptor.charAt(currentOffset);
        }
        currentChar = methodDescriptor.charAt(currentOffset + 1);
        if (currentChar == 'V') {
            return argumentsSize << 2;
        } else {
            int returnSize = (currentChar == 'J' || currentChar == 'D') ? 2 : 1;
            return argumentsSize << 2 | returnSize;
        }
    }
}
