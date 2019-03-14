package lsieun.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.exceptions.ClassFormatException;

/**
 * This class represents the field info structure, i.e., the representation
 * for a variable in the class. See JVM specification for details.
 *
 */
public final class Field extends FieldOrMethod {
    /**
     * Construct object from file stream.
     * @param file Input stream
     */
    Field(final DataInput file, final ConstantPool constant_pool) throws IOException, ClassFormatException {
        super(file, constant_pool);
    }
}
