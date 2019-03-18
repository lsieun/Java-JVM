package lsieun.bcel.classfile.attributes;

import java.util.List;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Node;

/**
 * represents one annotation in the annotation table
 *
 */
public class AnnotationEntry implements Node {

    private final int type_index;
    private final ConstantPool constant_pool;
    private final boolean isRuntimeVisible;

    private List<ElementValuePair> element_value_pairs;
}
