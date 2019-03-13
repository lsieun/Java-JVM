package lsieun.bcel.classfile.cp;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a (external) class.
 *
 * @see     Constant
 */
public final class ConstantClass extends Constant {
    // Identical to ConstantString except for the name
    private int name_index;
}
