package lsieun.bytecode.classfile.attrs.code;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cst.StackMapConst;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class StackMapFrame {
    private int frame_type;
    private int byte_code_offset;
    private StackMapType[] types_of_locals;
    private StackMapType[] types_of_stack_items;

    StackMapFrame(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] frame_type_bytes = byteDashboard.nextN(1);
        this.frame_type = ByteUtils.bytesToInt(frame_type_bytes, 0);
        this.byte_code_offset = 0;
        this.types_of_locals = new StackMapType[0];
        this.types_of_stack_items = new StackMapType[0];

        if(frame_type >= StackMapConst.SAME_FRAME && frame_type <= StackMapConst.SAME_FRAME_MAX) {
            byte_code_offset = frame_type - StackMapConst.SAME_FRAME;
        }
        else if (frame_type >= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME &&
                frame_type <= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            byte_code_offset = frame_type - StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME;
            types_of_stack_items = new StackMapType[1];
            types_of_stack_items[0] = new StackMapType(byteDashboard, constantPool);
        }
        else if (frame_type == StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            byte_code_offset = byteDashboard.nextShort();
            types_of_stack_items = new StackMapType[1];
            types_of_stack_items[0] = new StackMapType(byteDashboard, constantPool);
        }
        else if (frame_type >= StackMapConst.CHOP_FRAME && frame_type <= StackMapConst.CHOP_FRAME_MAX) {
            byte_code_offset = byteDashboard.nextShort();
        }
        else if (frame_type == StackMapConst.SAME_FRAME_EXTENDED) {
            byte_code_offset = byteDashboard.nextShort();
        }
        else if (frame_type >= StackMapConst.APPEND_FRAME && frame_type <= StackMapConst.APPEND_FRAME_MAX) {
            byte_code_offset = byteDashboard.nextShort();
            final int number_of_locals = frame_type - 251;
            types_of_locals = new StackMapType[number_of_locals];
            for (int i = 0; i < number_of_locals; i++) {
                types_of_locals[i] = new StackMapType(byteDashboard, constantPool);
            }
        }
        else if (frame_type == StackMapConst.FULL_FRAME) {
            byte_code_offset = byteDashboard.nextShort();
            final int number_of_locals = byteDashboard.nextShort();
            types_of_locals = new StackMapType[number_of_locals];
            for (int i = 0; i < number_of_locals; i++) {
                types_of_locals[i] = new StackMapType(byteDashboard, constantPool);
            }
            final int number_of_stack_items = byteDashboard.nextShort();
            types_of_stack_items = new StackMapType[number_of_stack_items];
            for (int i = 0; i < number_of_stack_items; i++) {
                types_of_stack_items[i] = new StackMapType(byteDashboard, constantPool);
            }
        }
        else {
            /* Can't happen */
            throw new ClassFormatException("Invalid frame type found while parsing stack map table: " + frame_type);
        }
    }

    public int getFrameType() {
        return frame_type;
    }

    public void setFrameType(int frame_type) {
        this.frame_type = frame_type;
    }

    public int getByteCodeOffset() {
        return byte_code_offset;
    }

    public void setByteCodeOffset(int byte_code_offset) {
        this.byte_code_offset = byte_code_offset;
    }

    public StackMapType[] getTypesOfLocals() {
        return types_of_locals;
    }

    public void setTypesOfLocals(StackMapType[] types_of_locals) {
        this.types_of_locals = types_of_locals;
    }

    public StackMapType[] getTypesOfStackItems() {
        return types_of_stack_items;
    }

    public void setTypesOfStackItems(StackMapType[] types_of_stack_items) {
        this.types_of_stack_items = types_of_stack_items;
    }

    public String getLocalsString() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append("locals=[");
        String value = stackMapTypes2String(types_of_locals);
        buf.append(value);
        buf.append("]");
        return buf.toString();
    }

    public String getStackItemsString() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append("stack_items=[");
        String value = stackMapTypes2String(types_of_stack_items);
        buf.append(value);
        buf.append("]");
        return buf.toString();
    }

    private String stackMapTypes2String(StackMapType[] array) {
        final StringBuilder buf = new StringBuilder(64);

        for (int i = 0; i < array.length; i++) {
            StackMapType item = array[i];
            String str = item.toString();
            buf.append(str);
            if (i < array.length - 1) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }
}
