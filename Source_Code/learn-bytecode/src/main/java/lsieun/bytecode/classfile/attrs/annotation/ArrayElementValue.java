package lsieun.bytecode.classfile.attrs.annotation;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class ArrayElementValue extends ElementValue {
    private final int num_values;
    private final List<ElementValue> element_value_list;
    private final String value;

    public ArrayElementValue(ByteDashboard byteDashboard, final ConstantPool constantPool) {
        super(byteDashboard);

        byte[] num_values_bytes = byteDashboard.nextN(2);
        this.num_values = ByteUtils.bytesToInt(num_values_bytes, 0);

        this.element_value_list = new ArrayList();
        for(int i=0; i<num_values; i++) {
            ElementValue item = ElementValue.readElementValue(byteDashboard, constantPool);
            this.element_value_list.add(item);
        }

        this.value = "//FIXME: 这里value不对啊";
    }

    @Override
    public String stringifyValue() {
        return this.value;
    }
}
