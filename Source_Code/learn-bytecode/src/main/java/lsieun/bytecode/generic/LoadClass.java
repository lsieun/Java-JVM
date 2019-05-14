package lsieun.bytecode.generic;

import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.Type;

/**
 * Denotes that an instruction may start the process of loading and resolving
 * the referenced class in the Virtual Machine.
 *
 */
public interface LoadClass {

    /**
     * Returns the ObjectType of the referenced class or interface
     * that may be loaded and resolved.
     *
     * @return object type that may be loaded or null if a primitive is
     * referenced
     */
    ObjectType getLoadClassType(ConstantPoolGen cpg);


    /**
     * Returns the type associated with this instruction.
     * LoadClass instances are always typed, but this type
     * does not always refer to the type of the class or interface
     * that it possibly forces to load. For example, GETFIELD would
     * return the type of the field and not the type of the class
     * where the field is defined.
     * If no class is forced to be loaded, <B>null</B> is returned.
     * An example for this is an ANEWARRAY instruction that creates
     * an int[][].
     *
     * @see #getLoadClassType(ConstantPoolGen)
     */
    Type getType(ConstantPoolGen cpg);
}
