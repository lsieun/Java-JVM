package lsieun.bcel.classfile;

public class JavaClass implements Node {

    private int major;
    private int minor; // Compiler version

    private int class_name_index;
    private int superclass_name_index;
    private String class_name;
    private String superclass_name;

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitJavaClass(this);
    }
}
