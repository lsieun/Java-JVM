package lsieun.bcel.classfile.attributes;

import lsieun.bcel.classfile.ConstantPool;

public class ArrayElementValue extends ElementValue {
    // For array types, this is the array
    private final ElementValue[] evalues;

    public ArrayElementValue(final int type, final ElementValue[] datums, final ConstantPool cpool) {
        super(type, cpool);
        if (type != ARRAY) {
            throw new RuntimeException(
                    "Only element values of type array can be built with this ctor - type specified: " + type);
        }
        this.evalues = datums;
    }

    public ElementValue[] getElementValuesArray() {
        return evalues;
    }

    public int getElementValuesArraySize() {
        return evalues.length;
    }

    @Override
    public String stringifyValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < evalues.length; i++) {
            sb.append(evalues[i].stringifyValue());
            if ((i + 1) < evalues.length) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < evalues.length; i++) {
            sb.append(evalues[i]);
            if ((i + 1) < evalues.length) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
