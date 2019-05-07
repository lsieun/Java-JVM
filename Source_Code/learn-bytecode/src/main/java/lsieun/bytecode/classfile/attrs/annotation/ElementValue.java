package lsieun.bytecode.classfile.attrs.annotation;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public abstract class ElementValue {
    public static final byte STRING = 's';
    public static final byte ENUM_CONSTANT = 'e';
    public static final byte CLASS = 'c';
    public static final byte ANNOTATION = '@';
    public static final byte ARRAY = '[';
    public static final byte PRIMITIVE_INT = 'I';
    public static final byte PRIMITIVE_BYTE = 'B';
    public static final byte PRIMITIVE_CHAR = 'C';
    public static final byte PRIMITIVE_DOUBLE = 'D';
    public static final byte PRIMITIVE_FLOAT = 'F';
    public static final byte PRIMITIVE_LONG = 'J';
    public static final byte PRIMITIVE_SHORT = 'S';
    public static final byte PRIMITIVE_BOOLEAN = 'Z';

    private final int type;
    public ElementValue(final ByteDashboard byteDashboard) {
        byte[] type_bytes = byteDashboard.nextN(1);
        this.type = ByteUtils.bytesToInt(type_bytes, 0);
    }

    public int getType() {
        return type;
    }

    public abstract String stringifyValue();

    public String toShortString() {
        return stringifyValue();
    }

    @Override
    public String toString() {
        return stringifyValue();
    }

    public static ElementValue readElementValue(final ByteDashboard byteDashboard, final ConstantPool constantPool) {
        final byte tag = byteDashboard.peek();

        switch (tag) {
            case PRIMITIVE_BYTE:
            case PRIMITIVE_CHAR:
            case PRIMITIVE_DOUBLE:
            case PRIMITIVE_FLOAT:
            case PRIMITIVE_INT:
            case PRIMITIVE_LONG:
            case PRIMITIVE_SHORT:
            case PRIMITIVE_BOOLEAN:
            case STRING:
                return new SimpleElementValue(byteDashboard, constantPool);
            case ENUM_CONSTANT:
                return new EnumElementValue(byteDashboard, constantPool);
            case CLASS:
                return new ClassElementValue(byteDashboard, constantPool);
            case ANNOTATION:
                // FIXME:考虑注解是否在Runtime时可见，即isRuntimeVisible
                return new AnnotationElementValue(byteDashboard, constantPool);
            case ARRAY:
                return new ArrayElementValue(byteDashboard, constantPool);

            default:
                throw new RuntimeException("Unexpected element value kind in annotation: " + tag);
        }
    }
}
