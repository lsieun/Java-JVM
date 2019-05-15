package lsieun.bytecode.classfile.visitors;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.clazz.MethodUtils;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public class MethodRawVisitor extends AbstractVisitor {
    private String nameAndType;

    public MethodRawVisitor(String nameAndType) {
        this.nameAndType = nameAndType;
    }

    @Override
    public void visitClassFile(ClassFile obj) {
        Methods methods = obj.getMethods();
        MethodInfo methodInfo = MethodUtils.findMethod(methods, nameAndType);
        if(methodInfo != null) {
            methodInfo.accept(this);
        }
    }

    @Override
    public void visitMethodInfo(MethodInfo obj) {
        byte[] bytes = obj.getBytes();
        ByteDashboard byteDashboard = new ByteDashboard("MethodInfo", bytes);

        byte[] access_flags_bytes = byteDashboard.nextN(2);
        int access_flags = ByteUtils.bytesToInt(access_flags_bytes, 0);
        System.out.printf("access_flags: %s (%d)\n", HexUtils.fromBytes(access_flags_bytes), access_flags);

        byte[] name_index_bytes = byteDashboard.nextN(2);
        int name_index = ByteUtils.bytesToInt(name_index_bytes, 0);
        System.out.printf("name_index: %s (%d)\n", HexUtils.fromBytes(name_index_bytes), name_index);

        byte[] descriptor_index_bytes = byteDashboard.nextN(2);
        int descriptor_index = ByteUtils.bytesToInt(descriptor_index_bytes, 0);
        System.out.printf("descriptor_index: %s (%d)\n", HexUtils.fromBytes(descriptor_index_bytes), descriptor_index);

        byte[] attributes_count_bytes = byteDashboard.nextN(2);
        int attributes_count = ByteUtils.bytesToInt(attributes_count_bytes, 0);
        System.out.printf("attributes_count: %s (%d)\n", HexUtils.fromBytes(attributes_count_bytes), attributes_count);

        Attributes attributes = obj.getAttributes();
        AttributeInfo[] entries = attributes.getEntries();
        for(AttributeInfo item : entries) {
            String name = item.getName();
            String hexCode = item.getHexCode();
            String prettyFormat = HexUtils.getPrettyFormat(hexCode);
            String format = "\n%s:\n%s";
            String line = String.format(format, name, prettyFormat);
            System.out.println(line);
        }
    }
}
