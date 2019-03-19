package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;

/**
 * base class for annotations
 *
 * @version $Id: Annotations
 */
public abstract class Annotations extends Attribute {
    private AnnotationEntry[] annotation_table;
    private final boolean isRuntimeVisible;

    /**
     * @param annotation_type the subclass type of the annotation
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     */
    Annotations(final byte annotation_type, final int name_index, final int length, final DataInput input,
                final ConstantPool constant_pool, final boolean isRuntimeVisible) throws IOException {
        this(annotation_type, name_index, length, (AnnotationEntry[]) null, constant_pool, isRuntimeVisible);
        final int annotation_table_length = input.readUnsignedShort();
        annotation_table = new AnnotationEntry[annotation_table_length];
        for (int i = 0; i < annotation_table_length; i++) {
            annotation_table[i] = AnnotationEntry.read(input, constant_pool, isRuntimeVisible);
        }
    }

    /**
     * @param annotation_type the subclass type of the annotation
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param annotation_table the actual annotations
     * @param constant_pool Array of constants
     */
    public Annotations(final byte annotation_type, final int name_index, final int length, final AnnotationEntry[] annotation_table,
                       final ConstantPool constant_pool, final boolean isRuntimeVisible) {
        super(annotation_type, name_index, length, constant_pool);
        this.annotation_table = annotation_table;
        this.isRuntimeVisible = isRuntimeVisible;
    }

    /**
     * returns the array of annotation entries in this annotation
     */
    public AnnotationEntry[] getAnnotationEntries() {
        return annotation_table;
    }

    /**
     * @return the number of annotation entries in this annotation
     */
    public final int getNumAnnotations() {
        if (annotation_table == null) {
            return 0;
        }
        return annotation_table.length;
    }

    public boolean isRuntimeVisible() {
        return isRuntimeVisible;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely defined by the contents of a Java class.
     * I.e., the hierarchy of methods, fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitAnnotation(this);
    }
}
