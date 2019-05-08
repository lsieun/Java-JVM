package lsieun.bytecode.classfile.visitors;

import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.attrs.RuntimeVisibleAnnotations;
import lsieun.bytecode.classfile.attrs.annotation.AnnotationEntry;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

/**
 * AttributeVisitor的目的，在于从“微观”角度去理解各种Attribute
 * 这里讲到了“微观”，那么什么是“微观”呢？我们先来谈谈“一般观”或“粗粒度观”或者“不微观”。
 * “一般观”，就是这个Attribute表示Deprecated或者SourceFile，它的值是多少，这是便于人理解的角度。
 * 而“微观”，就是将这个Attribute的bytecode进行拆分，这几个bytecode表示attribute_name_index，
 * 那个几bytecode代表attribute_length，这是从程序（bytecode）分析的角度来说。
 */
public abstract class AttributeVisitor extends AbstractVisitor {

    public AttributeVisitor() {
    }

    @Override
    public void visitClassFile(ClassFile obj) {
        obj.getConstantPool().accept(this);
    }

    // region ClassFile/Field/Method

    @Override
    public void visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations obj) {
        System.out.println("RuntimeVisibleAnnotations");
        System.out.printf("HexCode='%s'\n", obj.getHexCode());
        byte[] bytes = obj.getBytes();
        ByteDashboard byteDashboard = new ByteDashboard("RuntimeVisibleAnnotations", bytes);
        int attribute_length = processAttributeHeader(byteDashboard);

        int num_annotations = obj.getNumAnnotations();
        byte[] num_annotations_bytes = byteDashboard.nextN(2);

        System.out.printf("num_annotations='0x%s' (%d)\n", HexUtils.fromBytes(num_annotations_bytes), num_annotations);
        if(num_annotations > 0) {
            AnnotationEntry[] annotations = obj.getAnnotations();
            for(int i=0; i<num_annotations; i++) {
                AnnotationEntry item = annotations[i];
                int type_index = item.getTypeIndex();
                int num_element_value_pairs = item.getNumElementValuePairs();
                byte[] type_index_bytes = byteDashboard.nextN(2);
                byte[] num_element_value_pairs_bytes = byteDashboard.nextN(2);
                System.out.println("annotation {");
                System.out.printf("    type_index='0x%s' (%d)\n", HexUtils.fromBytes(type_index_bytes), type_index);
                System.out.printf("    num_element_value_pairs='0x%s' (%d)\n", HexUtils.fromBytes(num_element_value_pairs_bytes), num_element_value_pairs);
                System.out.println("}");
            }
        }

    }


    // endregion

    // region protected methods

    protected int processAttributeHeader(ByteDashboard byteDashboard) {
        byte[] attribute_name_index_bytes = byteDashboard.nextN(2);
        int attribute_name_index = ByteUtils.bytesToInt(attribute_name_index_bytes, 0);
        System.out.printf("attribute_name_index='0x%s' (%d)\n", HexUtils.fromBytes(attribute_name_index_bytes), attribute_name_index);

        byte[] attribute_length_bytes = byteDashboard.nextN(4);
        int attribute_length = ByteUtils.bytesToInt(attribute_length_bytes, 0);
        System.out.printf("attribte_length='0x%s' (%d)\n", HexUtils.fromBytes(attribute_length_bytes), attribute_length);
        return attribute_length;
    }
    // endregion


}
