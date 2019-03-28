package lsieun.bytecode.classfile.attrs.annotation;

import java.util.List;

public class Annotation {
    private final int type_index;
    private final int num_element_value_pairs;
    private final List<ElementValuePair> element_value_pair_list;
}
