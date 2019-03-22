package lsieun.bcel.classfile.consts;

public class CPConst {
    /*
     * The description of the constant pool is at:
     * http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4
     * References below are to the individual sections
     */

    /**
     * Marks a constant pool entry as type UTF-8.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.7">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Utf8               = 1;

    /**
     * Marks a constant pool entry as type Integer.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Integer            = 3;

    /**
     * Marks a constant pool entry as type Float.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Float              = 4;

    /**
     * Marks a constant pool entry as type Long.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Long               = 5;

    /**
     * Marks a constant pool entry as type Double.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Double             = 6;

    /**
     * Marks a constant pool entry as a Class
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.1">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Class              = 7;

    /**
     * Marks a constant pool entry as a Field Reference.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Fieldref           = 9;

    /**
     * Marks a constant pool entry as type String
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.3">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_String             = 8;

    /** Marks a constant pool entry as a Method Reference.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
     * The Constant Pool in The Java Virtual Machine Specification</a> */
    public static final byte CONSTANT_Methodref          = 10;

    /**
     * Marks a constant pool entry as an Interface Method Reference.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_InterfaceMethodref = 11;

    /** Marks a constant pool entry as a name and type.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6">
     * The Constant Pool in The Java Virtual Machine Specification</a> */
    public static final byte CONSTANT_NameAndType        = 12;

    /**
     * Marks a constant pool entry as a Method Handle.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.8">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_MethodHandle       = 15;

    /**
     * Marks a constant pool entry as a Method Type.
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.9">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_MethodType         = 16;

    /**
     * Marks a constant pool entry as dynamically computed.
     * @see  <a href="https://bugs.openjdk.java.net/secure/attachment/74618/constant-dynamic.html">
     * Change request for JEP 309</a>
     */
    public static final byte CONSTANT_Dynamic            = 17;

    /**
     * Marks a constant pool entry as an Invoke Dynamic
     * @see  <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.10">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_InvokeDynamic      = 18;

    /**
     * Marks a constant pool entry as a Module Reference.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.4.11">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Module             = 19;

    /**
     * Marks a constant pool entry as a Package Reference.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.4.12">
     * The Constant Pool in The Java Virtual Machine Specification</a>
     */
    public static final byte CONSTANT_Package            = 20;

    /**
     * The names of the types of entries in a constant pool.
     * Use getConstantName instead
     */
    private static final String[] CONSTANT_NAMES = {
            "", "CONSTANT_Utf8", "", "CONSTANT_Integer",
            "CONSTANT_Float", "CONSTANT_Long", "CONSTANT_Double",
            "CONSTANT_Class", "CONSTANT_String", "CONSTANT_Fieldref",
            "CONSTANT_Methodref", "CONSTANT_InterfaceMethodref",
            "CONSTANT_NameAndType", "", "", "CONSTANT_MethodHandle",
            "CONSTANT_MethodType", "CONSTANT_Dynamic", "CONSTANT_InvokeDynamic",
            "CONSTANT_Module", "CONSTANT_Package"};

    /**
     *
     * @param index
     * @return the CONSTANT_NAMES entry at the given index
     */
    public static String getConstantName(final int index) {
        return CONSTANT_NAMES[index];
    }
}
