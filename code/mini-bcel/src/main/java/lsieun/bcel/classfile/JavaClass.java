package lsieun.bcel.classfile;

public class JavaClass implements Node {

    private int major;
    private int minor; // Compiler version

    private int class_name_index;
    private int superclass_name_index;
    private String class_name;
    private String superclass_name;

    @Override
    public void accept(Visitor obj) {
        // TODO：还没有实现呢
    }
}
