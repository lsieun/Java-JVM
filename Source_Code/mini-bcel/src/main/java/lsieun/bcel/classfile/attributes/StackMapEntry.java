package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.StackMapConst;
import lsieun.bcel.exceptions.ClassFormatException;

/**
 * This class represents a stack map entry recording the types of
 * local variables and the the of stack items at a given byte code offset.
 * See CLDC specification 5.3.1.2
 *
 * @version $Id$
 * @see     StackMap
 * @see     StackMapType
 */
public final class StackMapEntry implements Node {
    private int frame_type;
    private int byte_code_offset;
    private StackMapType[] types_of_locals;
    private StackMapType[] types_of_stack_items;
    private ConstantPool constant_pool;

    /**
     * Construct object from input stream.
     *
     * @param input Input stream
     * @throws IOException
     */
    StackMapEntry(final DataInput input, final ConstantPool constantPool) throws IOException {
        this(input.readByte() & 0xFF, -1, null, null, constantPool);

        if (frame_type >= StackMapConst.SAME_FRAME && frame_type <= StackMapConst.SAME_FRAME_MAX) {
            byte_code_offset = frame_type - StackMapConst.SAME_FRAME;
        } else if (frame_type >= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME &&
                frame_type <= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            byte_code_offset = frame_type - StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME;
            types_of_stack_items = new StackMapType[1];
            types_of_stack_items[0] = new StackMapType(input, constantPool);
        } else if (frame_type == StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            byte_code_offset = input.readShort();
            types_of_stack_items = new StackMapType[1];
            types_of_stack_items[0] = new StackMapType(input, constantPool);
        } else if (frame_type >= StackMapConst.CHOP_FRAME && frame_type <= StackMapConst.CHOP_FRAME_MAX) {
            byte_code_offset = input.readShort();
        } else if (frame_type == StackMapConst.SAME_FRAME_EXTENDED) {
            byte_code_offset = input.readShort();
        } else if (frame_type >= StackMapConst.APPEND_FRAME && frame_type <= StackMapConst.APPEND_FRAME_MAX) {
            byte_code_offset = input.readShort();
            final int number_of_locals = frame_type - 251;
            types_of_locals = new StackMapType[number_of_locals];
            for (int i = 0; i < number_of_locals; i++) {
                types_of_locals[i] = new StackMapType(input, constantPool);
            }
        } else if (frame_type == StackMapConst.FULL_FRAME) {
            byte_code_offset = input.readShort();
            final int number_of_locals = input.readShort();
            types_of_locals = new StackMapType[number_of_locals];
            for (int i = 0; i < number_of_locals; i++) {
                types_of_locals[i] = new StackMapType(input, constantPool);
            }
            final int number_of_stack_items = input.readShort();
            types_of_stack_items = new StackMapType[number_of_stack_items];
            for (int i = 0; i < number_of_stack_items; i++) {
                types_of_stack_items[i] = new StackMapType(input, constantPool);
            }
        } else {
            /* Can't happen */
            throw new ClassFormatException("Invalid frame type found while parsing stack map table: " + frame_type);
        }
    }

    /**
     * Create an instance
     *
     * @param tag the frame_type to use
     * @param byteCodeOffset
     * @param typesOfLocals array of {@link StackMapType}s of locals
     * @param typesOfStackItems array ot {@link StackMapType}s of stack items
     * @param constantPool the constant pool
     */
    public StackMapEntry(final int tag, final int byteCodeOffset,
                         final StackMapType[] typesOfLocals,
                         final StackMapType[] typesOfStackItems, final ConstantPool constantPool) {
        this.frame_type = tag;
        this.byte_code_offset = byteCodeOffset;
        this.types_of_locals = typesOfLocals != null ? typesOfLocals : new StackMapType[0];
        this.types_of_stack_items = typesOfStackItems != null ? typesOfStackItems : new StackMapType[0];
        this.constant_pool = constantPool;
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append("(");
        if (frame_type >= StackMapConst.SAME_FRAME && frame_type <= StackMapConst.SAME_FRAME_MAX) {
            buf.append("SAME");
        } else if (frame_type >= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME &&
                frame_type <= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            buf.append("SAME_LOCALS_1_STACK");
        } else if (frame_type == StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            buf.append("SAME_LOCALS_1_STACK_EXTENDED");
        } else if (frame_type >= StackMapConst.CHOP_FRAME && frame_type <= StackMapConst.CHOP_FRAME_MAX) {
            buf.append("CHOP ").append(String.valueOf(251-frame_type));
        } else if (frame_type == StackMapConst.SAME_FRAME_EXTENDED) {
            buf.append("SAME_EXTENDED");
        } else if (frame_type >= StackMapConst.APPEND_FRAME && frame_type <= StackMapConst.APPEND_FRAME_MAX) {
            buf.append("APPEND ").append(String.valueOf(frame_type-251));
        } else if (frame_type == StackMapConst.FULL_FRAME) {
            buf.append("FULL");
        } else {
            buf.append("UNKNOWN (").append(frame_type).append(")");
        }
        buf.append(", offset delta=").append(byte_code_offset);
        if (types_of_locals.length > 0) {
            buf.append(", locals={");
            for (int i = 0; i < types_of_locals.length; i++) {
                buf.append(types_of_locals[i]);
                if (i < types_of_locals.length - 1) {
                    buf.append(", ");
                }
            }
            buf.append("}");
        }
        if (types_of_stack_items.length > 0) {
            buf.append(", stack items={");
            for (int i = 0; i < types_of_stack_items.length; i++) {
                buf.append(types_of_stack_items[i]);
                if (i < types_of_stack_items.length - 1) {
                    buf.append(", ");
                }
            }
            buf.append("}");
        }
        buf.append(")");
        return buf.toString();
    }

    /**
     * Calculate stack map entry size
     *
     */
    int getMapEntrySize() {
        if (frame_type >= StackMapConst.SAME_FRAME && frame_type <= StackMapConst.SAME_FRAME_MAX) {
            return 1;
        } else if (frame_type >= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME &&
                frame_type <= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            return 1 + (types_of_stack_items[0].hasIndex() ? 3 : 1);
        } else if (frame_type == StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            return 3 + (types_of_stack_items[0].hasIndex() ? 3 : 1);
        } else if (frame_type >= StackMapConst.CHOP_FRAME && frame_type <= StackMapConst.CHOP_FRAME_MAX) {
            return 3;
        } else if (frame_type == StackMapConst.SAME_FRAME_EXTENDED) {
            return 3;
        } else if (frame_type >= StackMapConst.APPEND_FRAME && frame_type <= StackMapConst.APPEND_FRAME_MAX) {
            int len = 3;
            for (final StackMapType types_of_local : types_of_locals) {
                len += types_of_local.hasIndex() ? 3 : 1;
            }
            return len;
        } else if (frame_type == StackMapConst.FULL_FRAME) {
            int len = 7;
            for (final StackMapType types_of_local : types_of_locals) {
                len += types_of_local.hasIndex() ? 3 : 1;
            }
            for (final StackMapType types_of_stack_item : types_of_stack_items) {
                len += types_of_stack_item.hasIndex() ? 3 : 1;
            }
            return len;
        } else {
            throw new RuntimeException("Invalid StackMap frame_type: " + frame_type);
        }
    }

    public int getFrameType() {
        return frame_type;
    }

    /**
     * @return Constant pool used by this object.
     */
    public final ConstantPool getConstantPool() {
        return constant_pool;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitStackMapEntry(this);
    }

}
