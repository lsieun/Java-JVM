package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.attrs.annotation.Annotation;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class RuntimeVisibleAnnotations extends AttributeInfo {
    private final int num_annotations;
    private final List<Annotation> annotation_list;

    public RuntimeVisibleAnnotations(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] num_annotations_bytes = byteDashboard.nextN(2);
        this.num_annotations = ByteUtils.bytesToInt(num_annotations_bytes, 0);

        this.annotation_list = new ArrayList();
        for(int i=0; i<num_annotations; i++) {
            Annotation item = new Annotation(byteDashboard, constantPool);
            this.annotation_list.add(item);
        }
    }
}
