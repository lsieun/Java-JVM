package lsieun.bcel.classfile.attributes;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

public class EnumElementValue extends ElementValue {
    // For enum types, these two indices point to the type and value
    private final int typeIdx;

    private final int valueIdx;

    public EnumElementValue(final int type, final int typeIdx, final int valueIdx, final ConstantPool cpool) {
        super(type, cpool);
        if (type != ENUM_CONSTANT) {
            throw new RuntimeException("Only element values of type enum can be built with this ctor - type specified: " + type);
        }
        this.typeIdx = typeIdx;
        this.valueIdx = valueIdx;
    }

    public int getValueIndex() {
        return valueIdx;
    }

    public int getTypeIndex() {
        return typeIdx;
    }

    public String getEnumTypeString() {
        final ConstantUtf8 cu8 = (ConstantUtf8) super.getConstantPool().getConstant(typeIdx, CPConst.CONSTANT_Utf8);
        return cu8.getBytes();// Utility.signatureToString(cu8.getBytes());
    }

    public String getEnumValueString() {
        final ConstantUtf8 cu8 = (ConstantUtf8) super.getConstantPool().getConstant(valueIdx, CPConst.CONSTANT_Utf8);
        return cu8.getBytes();
    }

    @Override
    public String stringifyValue() {
        final ConstantUtf8 cu8 = (ConstantUtf8) super.getConstantPool().getConstant(valueIdx, CPConst.CONSTANT_Utf8);
        return cu8.getBytes();
    }
}
