package lsieun.bcel.classfile.consts;

public class AttrConst {

    /** Attributes and their corresponding names.
     */
    public static final byte ATTR_UNKNOWN                                 = -1;
    public static final byte ATTR_SOURCE_FILE                             = 0;
    public static final byte ATTR_CONSTANT_VALUE                          = 1;
    public static final byte ATTR_CODE                                    = 2;
    public static final byte ATTR_EXCEPTIONS                              = 3;
    public static final byte ATTR_LINE_NUMBER_TABLE                       = 4;
    public static final byte ATTR_LOCAL_VARIABLE_TABLE                    = 5;
    public static final byte ATTR_INNER_CLASSES                           = 6;
    public static final byte ATTR_SYNTHETIC                               = 7;
    public static final byte ATTR_DEPRECATED                              = 8;
    public static final byte ATTR_PMG                                     = 9;
    public static final byte ATTR_SIGNATURE                               = 10;
    public static final byte ATTR_STACK_MAP                               = 11;
    public static final byte ATTR_RUNTIME_VISIBLE_ANNOTATIONS             = 12;
    public static final byte ATTR_RUNTIME_INVISIBLE_ANNOTATIONS           = 13;
    public static final byte ATTR_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS   = 14;
    public static final byte ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = 15;
    public static final byte ATTR_ANNOTATION_DEFAULT                      = 16;
    public static final byte ATTR_LOCAL_VARIABLE_TYPE_TABLE               = 17;
    public static final byte ATTR_ENCLOSING_METHOD                        = 18;
    public static final byte ATTR_STACK_MAP_TABLE                         = 19;
    public static final byte ATTR_BOOTSTRAP_METHODS                       = 20;
    public static final byte ATTR_METHOD_PARAMETERS                       = 21;

    public static final short KNOWN_ATTRIBUTES = 22; // count of attributes

    private static final String[] ATTRIBUTE_NAMES = {
            "SourceFile", "ConstantValue", "Code", "Exceptions",
            "LineNumberTable", "LocalVariableTable",
            "InnerClasses", "Synthetic", "Deprecated",
            "PMGClass", "Signature", "StackMap",
            "RuntimeVisibleAnnotations", "RuntimeInvisibleAnnotations",
            "RuntimeVisibleParameterAnnotations", "RuntimeInvisibleParameterAnnotations",
            "AnnotationDefault", "LocalVariableTypeTable", "EnclosingMethod", "StackMapTable",
            "BootstrapMethods", "MethodParameters"
    };

    /**
     *
     * @param index
     * @return the attribute name
     */
    public static String getAttributeName(final int index) {
        return ATTRIBUTE_NAMES[index];
    }
}
