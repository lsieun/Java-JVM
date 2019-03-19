package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.consts.StackMapConst;

/**
 * This class represents the type of a local variable or item on stack
 * used in the StackMap entries.
 *
 * @version $Id$
 * @see     StackMapEntry
 * @see     StackMap
 * @see     StackMapConst
 */
public final class StackMapType {
    private byte type;
    private int index = -1; // Index to CONSTANT_Class or offset
    private ConstantPool constant_pool;

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     */
    StackMapType(final DataInput file, final ConstantPool constant_pool) throws IOException {
        this(file.readByte(), -1, constant_pool);
        if (hasIndex()) {
            this.index = file.readShort();
        }
        this.constant_pool = constant_pool;
    }


    /**
     * @param type type tag as defined in the Constants interface
     * @param index index to constant pool, or byte code offset
     */
    public StackMapType(final byte type, final int index, final ConstantPool constant_pool) {
        if ((type < StackMapConst.ITEM_Bogus) || (type > StackMapConst.ITEM_NewObject)) {
            throw new RuntimeException("Illegal type for StackMapType: " + type);
        }
        this.type = type;
        this.index = index;
        this.constant_pool = constant_pool;
    }

    public byte getType() {
        return type;
    }

    /** @return index to constant pool if type == ITEM_Object, or offset
     * in byte code, if type == ITEM_NewObject, and -1 otherwise
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return Constant pool used by this object.
     */
    public final ConstantPool getConstantPool() {
        return constant_pool;
    }

    /** @return true, if type is either ITEM_Object or ITEM_NewObject
     */
    public final boolean hasIndex() {
        return type == StackMapConst.ITEM_Object || type == StackMapConst.ITEM_NewObject;
    }

    private String printIndex() {
        if (type == StackMapConst.ITEM_Object) {
            if (index < 0) {
                return ", class=<unknown>";
            }
            return ", class=" + constant_pool.constantToString(index, CPConst.CONSTANT_Class);
        } else if (type == StackMapConst.ITEM_NewObject) {
            return ", offset=" + index;
        } else {
            return "";
        }
    }

    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return "(type=" + StackMapConst.getItemName(type) + printIndex() + ")";
    }
}
