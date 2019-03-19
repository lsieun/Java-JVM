package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;

/**
 * base class for parameter annotations
 *
 * @version $Id: ParameterAnnotations
 */
public abstract class ParameterAnnotations extends Attribute {
    /** Table of parameter annotations */
    private ParameterAnnotationEntry[] parameter_annotation_table;

    /**
     * @param parameter_annotation_type the subclass type of the parameter annotation
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     */
    ParameterAnnotations(final byte parameter_annotation_type, final int name_index, final int length,
                         final DataInput input, final ConstantPool constant_pool) throws IOException {
        this(parameter_annotation_type, name_index, length, (ParameterAnnotationEntry[]) null,
                constant_pool);
        final int num_parameters = input.readUnsignedByte();
        parameter_annotation_table = new ParameterAnnotationEntry[num_parameters];
        for (int i = 0; i < num_parameters; i++) {
            parameter_annotation_table[i] = new ParameterAnnotationEntry(input, constant_pool);
        }
    }

    /**
     * @param parameter_annotation_type the subclass type of the parameter annotation
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param parameter_annotation_table the actual parameter annotations
     * @param constant_pool Array of constants
     */
    public ParameterAnnotations(final byte parameter_annotation_type, final int name_index, final int length,
                                final ParameterAnnotationEntry[] parameter_annotation_table, final ConstantPool constant_pool) {
        super(parameter_annotation_type, name_index, length, constant_pool);
        this.parameter_annotation_table = parameter_annotation_table;
    }

    /**
     * @return the parameter annotation entry table
     */
    public final ParameterAnnotationEntry[] getParameterAnnotationTable() {
        return parameter_annotation_table;
    }

    /**
     * returns the array of parameter annotation entries in this parameter annotation
     */
    public ParameterAnnotationEntry[] getParameterAnnotationEntries() {
        return parameter_annotation_table;
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
        v.visitParameterAnnotation(this);
    }
}
