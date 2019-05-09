package lsieun.bytecode.classfile.attrs.annotation;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class AnnotationEntry {
    private final int type_index;
    private final int num_element_value_pairs;
    private final List<ElementValuePair> element_value_pair_list;
    private final String value;

    public AnnotationEntry(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] type_index_bytes = byteDashboard.nextN(2);
        byte[] num_element_value_pairs_bytes = byteDashboard.nextN(2);

        this.type_index = ByteUtils.bytesToInt(type_index_bytes, 0);
        this.num_element_value_pairs = ByteUtils.bytesToInt(num_element_value_pairs_bytes, 0);
        this.element_value_pair_list = new ArrayList();
        for(int i=0; i<num_element_value_pairs; i++) {
            ElementValuePair item = new ElementValuePair(byteDashboard, constantPool);
            this.element_value_pair_list.add(item);
        }

        String type = constantPool.getConstantString(type_index, CPConst.CONSTANT_Utf8);
        this.value = type;
    }

    public int getTypeIndex() {
        return type_index;
    }

    public int getNumElementValuePairs() {
        return num_element_value_pairs;
    }

    public List<ElementValuePair> getElementValuePairList() {
        return element_value_pair_list;
    }

    public String getValue() {
        return value;
    }
}
