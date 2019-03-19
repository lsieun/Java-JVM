package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Visitor;

/**
 * represents one parameter annotation in the parameter annotation table
 *
 * @version $Id: ParameterAnnotationEntry
 */
public class ParameterAnnotationEntry implements Node {
    private final AnnotationEntry[] annotation_table;

    /**
     * Construct object from input stream.
     *
     * @param input Input stream
     * @throws IOException
     */
    ParameterAnnotationEntry(final DataInput input, final ConstantPool constant_pool) throws IOException {
        final int annotation_table_length = input.readUnsignedShort();
        annotation_table = new AnnotationEntry[annotation_table_length];
        for (int i = 0; i < annotation_table_length; i++) {
            // TODO isRuntimeVisible 这是原来的注释
            annotation_table[i] = AnnotationEntry.read(input, constant_pool, false);
        }
    }

    /**
     * returns the array of annotation entries in this annotation
     */
    public AnnotationEntry[] getAnnotationEntries() {
        return annotation_table;
    }

    public static ParameterAnnotationEntry[] createParameterAnnotationEntries(final Attribute[] attrs) {
        // Find attributes that contain parameter annotation data
        final List<ParameterAnnotationEntry> accumulatedAnnotations = new ArrayList<>(attrs.length);
        for (final Attribute attribute : attrs) {
            if (attribute instanceof ParameterAnnotations) {
                final ParameterAnnotations runtimeAnnotations = (ParameterAnnotations)attribute;
                Collections.addAll(accumulatedAnnotations, runtimeAnnotations.getParameterAnnotationEntries());
            }
        }
        return accumulatedAnnotations.toArray(new ParameterAnnotationEntry[accumulatedAnnotations.size()]);
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitParameterAnnotationEntry(this);
    }
}
