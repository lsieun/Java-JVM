package lsieun.bcel.classfile.consts;

public class MethodHandleConst {
    // Constants defining the behavior of the Method Handles (JVMS 5.4.3.5)

    public static final byte REF_getField         = 1;
    public static final byte REF_getStatic        = 2;
    public static final byte REF_putField         = 3;
    public static final byte REF_putStatic        = 4;
    public static final byte REF_invokeVirtual    = 5;
    public static final byte REF_invokeStatic     = 6;
    public static final byte REF_invokeSpecial    = 7;
    public static final byte REF_newInvokeSpecial = 8;
    public static final byte REF_invokeInterface  = 9;

    /**
     * The names of the reference_kinds of a CONSTANT_MethodHandle_info.
     */
    private static final String[] METHODHANDLE_NAMES = {
            "", "getField", "getStatic", "putField", "putStatic", "invokeVirtual",
            "invokeStatic", "invokeSpecial", "newInvokeSpecial", "invokeInterface" };

    /**
     *
     * @param index
     * @return the method handle name
     * @since 6.0
     */
    public static String getMethodHandleName(final int index) {
        return METHODHANDLE_NAMES[index];
    }
}
