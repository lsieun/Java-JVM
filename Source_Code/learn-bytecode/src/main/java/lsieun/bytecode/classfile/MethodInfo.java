package lsieun.bytecode.classfile;

import lsieun.bytecode.classfile.basic.AccessConst;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class MethodInfo extends Node {
    private final int access_flags;
    private final int name_index;
    private final int descriptor_index;
    private final int attributes_count;
    private final Attributes attributes;
    private String value;

    public MethodInfo(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] access_flags_bytes = byteDashboard.nextN(2);
        byte[] name_index_bytes = byteDashboard.nextN(2);
        byte[] descriptor_index_bytes = byteDashboard.nextN(2);
        byte[] attributes_count_bytes = byteDashboard.nextN(2);

        this.access_flags = ByteUtils.bytesToInt(access_flags_bytes, 0);
        this.name_index = ByteUtils.bytesToInt(name_index_bytes, 0);
        this.descriptor_index = ByteUtils.bytesToInt(descriptor_index_bytes, 0);
        this.attributes_count = ByteUtils.bytesToInt(attributes_count_bytes, 0);
        //this.value = "#" + name_index + ":#" + descriptor_index;
        this.value = constantPool.getConstantString(name_index, CPConst.CONSTANT_Utf8) + ":"
                + constantPool.getConstantString(descriptor_index, CPConst.CONSTANT_Utf8);

        byte[] bytes = ByteUtils.merge(access_flags_bytes, name_index_bytes, descriptor_index_bytes, attributes_count_bytes);

        this.attributes = new Attributes(byteDashboard, attributes_count, constantPool);
        for(int i=0; i<attributes_count; i++) {
            AttributeInfo attr = this.attributes.getEntries()[i];
            bytes = ByteUtils.merge(bytes, attr.getBytes());
        }

        // 设置bytes
        super.setBytes(bytes);
    }

    public int getAccessFlags() {
        return access_flags;
    }

    public String getAccessFlagsString() {
        return AccessConst.getMethodAccessFlagsString(access_flags);
    }

    public int getNameIndex() {
        return name_index;
    }

    public int getDescriptorIndex() {
        return descriptor_index;
    }

    public int getAttributesCount() {
        return attributes_count;
    }

    public Attributes getAttributes() {
        return attributes;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitMethodInfo(this);
    }

}
