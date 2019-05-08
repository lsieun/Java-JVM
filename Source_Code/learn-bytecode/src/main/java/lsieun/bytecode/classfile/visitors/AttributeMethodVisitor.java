package lsieun.bytecode.classfile.visitors;

import java.util.List;

import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.InstructionParser;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public class AttributeMethodVisitor extends AttributeVisitor {
    // region Method

    @Override
    public void visitCode(Code obj) {
        String name = obj.getName();
        String hexCode = obj.getHexCode();
        byte[] bytes = obj.getBytes();
        System.out.println(name);

        String formatTwo = "HexCode:\n%s\n";
        String secondLine = String.format(formatTwo, HexUtils.getPrettyFormat(hexCode));
        System.out.println(secondLine);

        ByteDashboard byteDashboard = new ByteDashboard("CodeAttribute", bytes);
        int attribute_length = processAttributeHeader(byteDashboard);

        byte[] max_stack_bytes = byteDashboard.nextN(2);
        int max_stack = ByteUtils.bytesToInt(max_stack_bytes, 0);
        System.out.printf("max_stack='%s' (%d)\n", HexUtils.fromBytes(max_stack_bytes), max_stack);

        byte[] max_locals_bytes = byteDashboard.nextN(2);
        int max_locals = ByteUtils.bytesToInt(max_locals_bytes, 0);
        System.out.printf("max_locals='%s' (%d)\n", HexUtils.fromBytes(max_locals_bytes), max_locals);

        byte[] code_length_bytes = byteDashboard.nextN(4);
        int code_length = ByteUtils.bytesToInt(code_length_bytes, 0);
        System.out.printf("code_length='%s' (%d)\n", HexUtils.fromBytes(code_length_bytes), code_length);

        byte[] code_bytes = byteDashboard.nextN(code_length);
        System.out.printf("code='%s'\n", HexUtils.fromBytes(code_bytes));
        List<String> lines = InstructionParser.bytes2Lines(code_bytes);
        for(String line : lines) {
            System.out.println(line);
        }


        byte[] exception_table_length_bytes = byteDashboard.nextN(2);
        int exception_table_length = ByteUtils.bytesToInt(exception_table_length_bytes, 0);
        System.out.printf("exception_table_length='%s' (%d)\n", HexUtils.fromBytes(exception_table_length_bytes), exception_table_length);
        for (int i = 0; i < exception_table_length; i++) {
            byte[] exception_info_bytes = byteDashboard.nextN(8);
            System.out.printf("exception_info='%s'\n", HexUtils.fromBytes(exception_info_bytes));
        }

        byte[] attributes_count_bytes = byteDashboard.nextN(2);
        int attributes_count = ByteUtils.bytesToInt(attributes_count_bytes, 0);
        System.out.printf("attributes_count='%s' (%d)\n", HexUtils.fromBytes(attributes_count_bytes), attributes_count);
        for (int i = 0; i < attributes_count; i++) {
            byte[] length_bytes = byteDashboard.peekN(2, 4);
            int length = ByteUtils.bytesToInt(length_bytes, 0);
            byte[] attr_bytes = byteDashboard.nextN(length + 6);
            System.out.printf("attribute_info='%s'\n", HexUtils.fromBytes(attr_bytes));
        }
    }

    // endregion
}
