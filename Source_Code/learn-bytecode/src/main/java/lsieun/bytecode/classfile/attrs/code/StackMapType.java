package lsieun.bytecode.classfile.attrs.code;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.classfile.basic.StackMapConst;
import lsieun.bytecode.utils.ByteDashboard;

public final class StackMapType {
    private byte type;
    private int index = -1; // Index to CONSTANT_Class or offset
    private ConstantPool constant_pool;

    StackMapType(ByteDashboard byteDashboard, ConstantPool constantPool) {
        this.type = byteDashboard.readByte();
        if (hasIndex()) {
            this.index = byteDashboard.nextShort();
        }
        this.constant_pool = constantPool;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
            return ", class=" + constant_pool.getConstantString(index, CPConst.CONSTANT_Class);
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
