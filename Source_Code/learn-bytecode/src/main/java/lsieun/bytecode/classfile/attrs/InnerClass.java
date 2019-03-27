package lsieun.bytecode.classfile.attrs;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.basic.AccessConst;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class InnerClass {
    private final int inner_class_info_index;
    private final int outer_class_info_index;
    private final int inner_name_index;
    private final int inner_class_access_flags;

    private final String name;
    private String value;

    public InnerClass(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] inner_class_info_index_bytes = byteDashboard.nextN(2);
        byte[] outer_class_info_index_bytes = byteDashboard.nextN(2);
        byte[] inner_name_index_bytes = byteDashboard.nextN(2);
        byte[] inner_class_access_flags_bytes = byteDashboard.nextN(2);

        this.inner_class_info_index = ByteUtils.bytesToInt(inner_class_info_index_bytes, 0);
        this.outer_class_info_index = ByteUtils.bytesToInt(outer_class_info_index_bytes, 0);
        this.inner_name_index = ByteUtils.bytesToInt(inner_name_index_bytes, 0);
        this.inner_class_access_flags = ByteUtils.bytesToInt(inner_class_access_flags_bytes, 0);

        this.value = "#" + inner_class_info_index + ",#" + outer_class_info_index
                   + ",#" + inner_name_index + ",#" + AccessConst.getClassAccessFlagsString(inner_class_access_flags);
        if(inner_name_index == 0) {
            // If the Class is anonymous, the value of the 'inner_name_index' item
            // must be zero.
            this.name = "<anonymous class>";
        }
        else {
            this.name = constantPool.getConstantString(inner_name_index, CPConst.CONSTANT_Utf8);
        }

    }

    public int getInnerClassInfoIndex() {
        return inner_class_info_index;
    }

    public int getOuterClassInfoIndex() {
        return outer_class_info_index;
    }

    public int getInnerNameIndex() {
        return inner_name_index;
    }

    public int getInnerClassAccessFlags() {
        return inner_class_access_flags;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "InnerClass{" +
                "value='" + value + '\'' +
                '}';
    }
}
