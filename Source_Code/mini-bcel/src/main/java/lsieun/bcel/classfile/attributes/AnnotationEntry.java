package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * represents one annotation in the annotation table
 *
 */
public class AnnotationEntry implements Node {

    private final int type_index;
    private final ConstantPool constant_pool;
    private final boolean isRuntimeVisible;

    private List<ElementValuePair> element_value_pairs;

    public AnnotationEntry(final int type_index, final ConstantPool constant_pool, final boolean isRuntimeVisible) {
        this.type_index = type_index;
        this.constant_pool = constant_pool;
        this.isRuntimeVisible = isRuntimeVisible;
    }

    public int getTypeIndex() {
        return type_index;
    }

    public ConstantPool getConstantPool() {
        return constant_pool;
    }

    public boolean isRuntimeVisible() {
        return isRuntimeVisible;
    }

    /**
     * @return the annotation type index
     */
    public int getAnnotationTypeIndex() {
        return type_index;
    }

    /**
     * @return the annotation type name
     */
    public String getAnnotationType() {
        final ConstantUtf8 c = (ConstantUtf8) constant_pool.getConstant(type_index, CPConst.CONSTANT_Utf8);
        return c.getBytes();
    }

    /**
     * @return the number of element value pairs in this annotation entry
     */
    public final int getNumElementValuePairs() {
        return element_value_pairs.size();
    }

    /**
     * @return the element value pairs in this annotation entry
     */
    public ElementValuePair[] getElementValuePairs() {
        // TODO return List 这是原来的注释，不是我写的注释
        return element_value_pairs.toArray(new ElementValuePair[element_value_pairs.size()]);
    }

    public void addElementNameValuePair(final ElementValuePair elementNameValuePair) {
        element_value_pairs.add(elementNameValuePair);
    }

    /*
     * Factory method to create an AnnotionEntry from a DataInput
     *
     * @param input
     * @param constant_pool
     * @param isRuntimeVisible
     * @return the entry
     * @throws IOException
     */
    public static AnnotationEntry read(final DataInput input, final ConstantPool constant_pool, final boolean isRuntimeVisible) throws IOException {

        final AnnotationEntry annotationEntry = new AnnotationEntry(input.readUnsignedShort(), constant_pool, isRuntimeVisible);
        final int num_element_value_pairs = input.readUnsignedShort();
        annotationEntry.element_value_pairs = new ArrayList<>();
        for (int i = 0; i < num_element_value_pairs; i++) {
            annotationEntry.element_value_pairs.add(
                    new ElementValuePair(input.readUnsignedShort(), ElementValue.readElementValue(input, constant_pool),
                            constant_pool));
        }
        return annotationEntry;
    }

    public static AnnotationEntry[] createAnnotationEntries(final Attribute[] attrs) {
        // Find attributes that contain annotation data
        final List<AnnotationEntry> accumulatedAnnotations = new ArrayList<>(attrs.length);
        for (final Attribute attribute : attrs) {
            if (attribute instanceof Annotations) {
                final Annotations runtimeAnnotations = (Annotations) attribute;
                Collections.addAll(accumulatedAnnotations, runtimeAnnotations.getAnnotationEntries());
            }
        }
        return accumulatedAnnotations.toArray(new AnnotationEntry[accumulatedAnnotations.size()]);
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely defined by the contents of a Java class.
     * I.e., the hierarchy of methods, fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitAnnotationEntry(this);
    }

    public String toShortString() {
        final StringBuilder result = new StringBuilder();
        result.append("@");
        result.append(getAnnotationType());
        final ElementValuePair[] evPairs = getElementValuePairs();
        if (evPairs.length > 0) {
            result.append("(");
            for (final ElementValuePair element : evPairs) {
                result.append(element.toShortString());
            }
            result.append(")");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return toShortString();
    }
}
