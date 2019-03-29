package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.attrs.annotation.AnnotationEntry;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class RuntimeVisibleAnnotations extends AttributeInfo {
    private final int num_annotations;
    private final List<AnnotationEntry> annotation_list;

    public RuntimeVisibleAnnotations(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] num_annotations_bytes = byteDashboard.nextN(2);
        this.num_annotations = ByteUtils.bytesToInt(num_annotations_bytes, 0);

        this.annotation_list = new ArrayList();
        for(int i=0; i<num_annotations; i++) {
            AnnotationEntry item = new AnnotationEntry(byteDashboard, constantPool);
            this.annotation_list.add(item);
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();

        List<String> annotation_str_list = new ArrayList();
        for(int i=0; i<num_annotations; i++) {
            AnnotationEntry item = this.annotation_list.get(i);
            String value = item.getValue();
            annotation_str_list.add(value);
        }
        list.add("Value='" + StringUtils.list2str(annotation_str_list, "[", "]", ",") + "'");

        list.add("AttributeNameIndex='" + this.getAttributeNameIndex() + "'");
        list.add("HexCode='" + super.getHexCode() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
