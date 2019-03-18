package lsieun.bcel.classfile.attributes;

/**
 * base class for annotations
 *
 * @version $Id: Annotations
 */
public abstract class Annotations extends Attribute {
    private AnnotationEntry[] annotation_table;
    private final boolean isRuntimeVisible;
}
