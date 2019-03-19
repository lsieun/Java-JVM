package lsieun.bcel.classfile.attributes;

import lsieun.bcel.classfile.ConstantPool;

public class AnnotationElementValue extends ElementValue {
    // For annotation element values, this is the annotation
    private final AnnotationEntry annotationEntry;

    public AnnotationElementValue(final int type, final AnnotationEntry annotationEntry, final ConstantPool cpool) {
        super(type, cpool);
        if (type != ANNOTATION) {
            throw new RuntimeException("Only element values of type annotation can be built with this ctor - type specified: " + type);
        }
        this.annotationEntry = annotationEntry;
    }

    public AnnotationEntry getAnnotationEntry() {
        return annotationEntry;
    }

    @Override
    public String stringifyValue() {
        return annotationEntry.toString();
    }

    @Override
    public String toString() {
        return stringifyValue();
    }
}
