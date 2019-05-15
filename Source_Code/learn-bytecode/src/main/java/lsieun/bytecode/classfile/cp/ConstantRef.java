package lsieun.bytecode.classfile.cp;

public class ConstantRef extends Constant {
    private int class_index;
    private int name_and_type_index;

    public ConstantRef(byte tag) {
        super(tag);
    }

    public int getClassIndex() {
        return class_index;
    }

    public void setClassIndex(int class_index) {
        this.class_index = class_index;
    }

    public int getNameAndTypeIndex() {
        return name_and_type_index;
    }

    public void setNameAndTypeIndex(int name_and_type_index) {
        this.name_and_type_index = name_and_type_index;
    }
}
