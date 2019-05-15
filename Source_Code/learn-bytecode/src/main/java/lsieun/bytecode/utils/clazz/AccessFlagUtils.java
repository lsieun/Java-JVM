package lsieun.bytecode.utils.clazz;

import lsieun.bytecode.generic.cst.AccessConst;

public class AccessFlagUtils {
    public static boolean isStatic(int accessFlags) {
        return ((AccessConst.ACC_STATIC & accessFlags) == AccessConst.ACC_STATIC);
    }
}
