package lsieun.bytecode.classfile.attrs.annotation;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;

public final class AnnotationElementValue extends ElementValue {
    // For annotation element values, this is the annotation
    private final AnnotationEntry annotationEntry;

    public AnnotationElementValue(ByteDashboard byteDashboard, final ConstantPool constantPool) {
        super(byteDashboard);

        this.annotationEntry = new AnnotationEntry(byteDashboard, constantPool);
    }

    @Override
    public String stringifyValue() {
        return null;
    }
}
