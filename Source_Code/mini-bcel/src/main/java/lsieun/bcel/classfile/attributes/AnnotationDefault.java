package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;


import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * Represents the default value of a annotation for a method info
 *
 * @version $Id: AnnotationDefault
 */
public class AnnotationDefault extends Attribute {
    private ElementValue default_value;

    /**
     * @param name_index    Index pointing to the name <em>Code</em>
     * @param length        Content length in bytes
     * @param input         Input stream
     * @param constant_pool Array of constants
     */
    AnnotationDefault(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool) throws IOException {
        this(name_index, length, (ElementValue) null, constant_pool);
        default_value = ElementValue.readElementValue(input, constant_pool);
    }

    /**
     * @param name_index    Index pointing to the name <em>Code</em>
     * @param length        Content length in bytes
     * @param defaultValue  the annotation's default value
     * @param constant_pool Array of constants
     */
    public AnnotationDefault(final int name_index, final int length, final ElementValue defaultValue, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_ANNOTATION_DEFAULT, name_index, length, constant_pool);
        this.default_value = defaultValue;
    }

    /**
     * @return the default value
     */
    public final ElementValue getDefaultValue() {
        return default_value;
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
        v.visitAnnotationDefault(this);
    }
}
