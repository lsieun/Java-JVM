package lsieun.bytecode.generic.type;

import lsieun.bytecode.generic.cnst.TypeConst;

/**
 * Denotes reference such as java.lang.String.
 */
public class ObjectType extends ReferenceType {
    private final String class_name; // Class name of type

    /**
     * @param class_name fully qualified class name, e.g. java.lang.String
     */
    public ObjectType(final String class_name) {
        super(TypeConst.T_OBJECT, "L" + class_name.replace('.', '/') + ";");
        this.class_name = class_name.replace('/', '.');
    }

    public String getClassName() {
        return class_name;
    }

    public static ObjectType getInstance(final String class_name) {
        return new ObjectType(class_name);
    }
}
