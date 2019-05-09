package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.classfile.cp.Constant;

/**
 * This class is used to build up a constant pool. The user adds
 * constants via `addXXX' methods, `addString', `addClass',
 * etc.. These methods return an index into the constant
 * pool. Finally, `getFinalConstantPool()' returns the constant pool
 * built up. Intermediate versions of the constant pool can be
 * obtained with `getConstantPool()'. A constant pool has capacity for
 * Constants.MAX_SHORT entries. Note that the first (0) is used by the
 * JVM and that Double and Long constants need two slots.
 *
 * @version $Id$
 * @see Constant
 */
public class ConstantPoolGen {

    private static final int DEFAULT_BUFFER_SIZE = 256;

    private int size;

    private Constant[] constants;
}
