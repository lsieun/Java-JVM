package lsieun.bytecode.generic.cst;

public class JVMConst {
    /**
     * Java class file format Magic number (0xCAFEBABE)
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.1-200-A">
     * The ClassFile Structure in The Java Virtual Machine Specification</a>
     */
    public static final int JVM_CLASSFILE_MAGIC = 0xCAFEBABE;

    /** Maximum value for an unsigned short.
     */
    public static final int MAX_SHORT = 65535; // 2^16 - 1

    /** Maximum value for an unsigned byte.
     */
    public static final int MAX_BYTE  = 255; // 2^8 - 1

    /**
     * Maximum Constant Pool entries.
     * One of the limitations of the Java Virtual Machine.
     * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.11-100-A">
     * The Java Virtual Machine Specification, Java SE 8 Edition, page 330, chapter 4.11.</a>
     */
    public static final int MAX_CP_ENTRIES     = 65535;

    /**
     * Maximum code size (plus one; the code size must be LESS than this)
     * One of the limitations of the Java Virtual Machine.
     * Note vmspec2 page 152 ("Limitations") says:
     * "The amount of code per non-native, non-abstract method is limited to 65536 bytes by
     * the sizes of the indices in the exception_table of the Code attribute (§4.7.3),
     * in the LineNumberTable attribute (§4.7.8), and in the LocalVariableTable attribute (§4.7.9)."
     * However this should be taken as an upper limit rather than the defined maximum.
     * On page 134 (4.8.1 Static Constants) of the same spec, it says:
     * "The value of the code_length item must be less than 65536."
     * The entry in the Limitations section has been removed from later versions of the spec;
     * it is not present in the Java SE 8 edition.
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3-300-E">
     * The Java Virtual Machine Specification, Java SE 8 Edition, page 104, chapter 4.7.</a>
     */
    public static final int MAX_CODE_SIZE      = 65536; //bytes

    /**
     * The maximum number of dimensions in an array ({@value}).
     * One of the limitations of the Java Virtual Machine.
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2-150">
     * Field Descriptors in The Java Virtual Machine Specification</a>
     */
    public static final int MAX_ARRAY_DIMENSIONS = 255;

    /** The name of the static initializer, also called &quot;class
     *  initialization method&quot; or &quot;interface initialization
     *   method&quot;. This is &quot;&lt;clinit&gt;&quot;.
     */
    public static final String STATIC_INITIALIZER_NAME = "<clinit>";

    /** The name of every constructor method in a class, also called
     * &quot;instance initialization method&quot;. This is &quot;&lt;init&gt;&quot;.
     */
    public static final String CONSTRUCTOR_NAME = "<init>";
}
