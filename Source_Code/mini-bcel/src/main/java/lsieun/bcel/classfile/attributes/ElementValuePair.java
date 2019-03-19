package lsieun.bcel.classfile.attributes;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * an annotation's element value pair
 */
public class ElementValuePair {
    private final int elementNameIndex;
    private final ElementValue elementValue;

    private final ConstantPool constantPool;


    public ElementValuePair(final int elementNameIndex, final ElementValue elementValue,
                            final ConstantPool constantPool) {
        this.elementValue = elementValue;
        this.elementNameIndex = elementNameIndex;
        this.constantPool = constantPool;
    }

    public int getNameIndex() {
        return elementNameIndex;
    }

    public final ElementValue getValue() {
        return elementValue;
    }

    public String getNameString() {
        final ConstantUtf8 c = (ConstantUtf8) constantPool.getConstant(elementNameIndex, CPConst.CONSTANT_Utf8);
        return c.getBytes();
    }

    public String toShortString() {
        final StringBuilder result = new StringBuilder();
        result.append(getNameString()).append("=").append(getValue().toShortString());
        return result.toString();
    }
}
