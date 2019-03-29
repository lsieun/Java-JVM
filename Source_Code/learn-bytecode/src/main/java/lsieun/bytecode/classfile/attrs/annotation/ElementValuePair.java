package lsieun.bytecode.classfile.attrs.annotation;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class ElementValuePair {
    private final int element_name_index;
    private final ElementValue value;

    public ElementValuePair(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] element_name_index_bytes = byteDashboard.nextN(2);

        this.element_name_index = ByteUtils.bytesToInt(element_name_index_bytes, 0);
        this.value = ElementValue.readElementValue(byteDashboard, constantPool);
    }
}
