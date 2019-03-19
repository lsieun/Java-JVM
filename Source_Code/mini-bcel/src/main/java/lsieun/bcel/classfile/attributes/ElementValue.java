package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;

/**
 * @version $Id: ElementValue
 */
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

    public static ElementValue readElementValue(final DataInput input, final ConstantPool cpool) throws IOException {
        final byte type = input.readByte();
        switch (type) {
            case PRIMITIVE_BYTE:
            case PRIMITIVE_CHAR:
            case PRIMITIVE_DOUBLE:
            case PRIMITIVE_FLOAT:
            case PRIMITIVE_INT:
            case PRIMITIVE_LONG:
            case PRIMITIVE_SHORT:
            case PRIMITIVE_BOOLEAN:
            case STRING:
                return new SimpleElementValue(type, input.readUnsignedShort(), cpool);

            case ENUM_CONSTANT:
                return new EnumElementValue(ENUM_CONSTANT, input.readUnsignedShort(), input.readUnsignedShort(), cpool);

            case CLASS:
                return new ClassElementValue(CLASS, input.readUnsignedShort(), cpool);

            case ANNOTATION:
                // TODO isRuntimeVisible  这是原来的注释，现在我还不明白是要做什么
                return new AnnotationElementValue(ANNOTATION, AnnotationEntry.read(input, cpool, false), cpool);

            case ARRAY:
                final int numArrayVals = input.readUnsignedShort();
                final ElementValue[] evalues = new ElementValue[numArrayVals];
                for (int j = 0; j < numArrayVals; j++) {
                    evalues[j] = ElementValue.readElementValue(input, cpool);
                }
                return new ArrayElementValue(ARRAY, evalues, cpool);

            default:
                throw new RuntimeException("Unexpected element value kind in annotation: " + type);
        }
    }

    private final int type;
    private final ConstantPool cpool;

    protected ElementValue(final int type, final ConstantPool cpool) {
        this.type = type;
        this.cpool = cpool;
    }

    public int getElementValueType() {
        return type;
    }

    final ConstantPool getConstantPool() {
        return cpool;
    }

    final int getType() {
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
}
