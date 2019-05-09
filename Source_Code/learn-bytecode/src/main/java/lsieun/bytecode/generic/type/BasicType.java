package lsieun.bytecode.generic.type;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cnst.TypeConst;

/**
 * Denotes basic type such as int.
 */
public final class BasicType extends Type {
    /**
     * Constructor for basic types such as int, long, `void'
     *
     * @param type one of T_INT, T_BOOLEAN, ..., T_VOID
     * @see TypeConst
     */
    BasicType(final byte type) {
        super(type, TypeConst.getShortTypeName(type));
        if ((type < TypeConst.T_BOOLEAN) || (type > TypeConst.T_VOID)) {
            throw new ClassGenException("Invalid type: " + type);
        }
    }

    public static BasicType getType(final byte type) {
        switch (type) {
            case TypeConst.T_VOID:
                return VOID;
            case TypeConst.T_BOOLEAN:
                return BOOLEAN;
            case TypeConst.T_BYTE:
                return BYTE;
            case TypeConst.T_SHORT:
                return SHORT;
            case TypeConst.T_CHAR:
                return CHAR;
            case TypeConst.T_INT:
                return INT;
            case TypeConst.T_LONG:
                return LONG;
            case TypeConst.T_DOUBLE:
                return DOUBLE;
            case TypeConst.T_FLOAT:
                return FLOAT;
            default:
                throw new ClassGenException("Invalid type: " + type);
        }
    }
}
