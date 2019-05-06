package lsieun.bytecode.classfile.visitors;

import java.util.List;

import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.attrs.field.ConstantValue;
import lsieun.bytecode.classfile.attrs.code.LineNumberTable;
import lsieun.bytecode.classfile.attrs.classfile.SourceFile;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.InstructionParser;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public class AttributeStandardVisitor extends AbstractVisitor {
    private boolean verbose;

    public AttributeStandardVisitor() {
    }

    public AttributeStandardVisitor(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public void visitClassFile(ClassFile obj) {
        if(verbose) {
            obj.getConstantPool().accept(this);
        }
    }

    // region ClassFile/Field/Method

    // endregion

    // region ClassFile

    @Override
    public void visitSourceFile(SourceFile obj) {
        String pattern = "attribute_name_index(u2)-attribte_length(u4)-sourcefile_index(u2)";

        String formatOne = "%s\nPattern='%s'\nHexCode='%s'";
        String firstLine = String.format(formatOne,
                obj.getName(), pattern, obj.getHexCode());
        System.out.println(firstLine);

        String formatTwo = "    attribute_name_index='%d'\n    attribte_length='%d'\n    sourcefile_index='%d'";
        String secondLine = String.format(formatTwo,
                obj.getAttributeNameIndex(),
                obj.getAttributeLength(),
                obj.getSourcefileIndex());
        System.out.println(secondLine);
    }

    // endregion

    // region Field

    @Override
    public void visitConstantValue(ConstantValue obj) {
        String pattern = "attribute_name_index(u2)-attribte_length(u4)-constantvalue_index(u2)";

        String formatOne = "%s\nPattern='%s'\nHexCode='%s'";
        String firstLine = String.format(formatOne,
                obj.getName(), pattern, obj.getHexCode());
        System.out.println(firstLine);

        String formatTwo = "    attribute_name_index='%d'\n    attribte_length='%d'\n    constantvalue_index='%d'";
        String secondLine = String.format(formatTwo,
                obj.getAttributeNameIndex(),
                obj.getAttributeLength(),
                obj.getConstantValueIndex());
        System.out.println(secondLine);
    }

    // endregion

    // region Method

    @Override
    public void visitCode(Code obj) {
        String pattern = "attribute_name_index(u2)-attribute_length(u4)" +
                "-max_stack(u2)-max_locals(2)-code_length(u4)-code[code_length](u1)" +
                "-exception_table_length(u2)-exception_table[exception_table_length](exception_info)" +
                "-attributes_count(u2)-attributes[attributes_count](attribute_info)";
        String[] array = pattern.split("-");
        StringBuilder formattedPattern = new StringBuilder();
        for (String str : array) {
            formattedPattern.append("    " + str + "\r\n");
        }

        String name = obj.getName();
        String hexCode = obj.getHexCode();
        byte[] bytes = obj.getBytes();
        System.out.println(name);

        String formatOne = "Pattern:\n%s";
        String firstLine = String.format(formatOne, formattedPattern);
        System.out.println(firstLine);

        String formatTwo = "HexCode:\n%s";
        String secondLine = String.format(formatTwo, HexUtils.getPrettyFormat(hexCode));
        System.out.println(secondLine);

        ByteDashboard byteDashboard = new ByteDashboard("CodeAttribute", bytes);
        int attribute_length = processAttributeHeader(byteDashboard);

        byte[] max_stack_bytes = byteDashboard.nextN(2);
        int max_stack = ByteUtils.bytesToInt(max_stack_bytes, 0);
        System.out.printf("max_stack: %s (%d)\n", HexUtils.fromBytes(max_stack_bytes), max_stack);

        byte[] max_locals_bytes = byteDashboard.nextN(2);
        int max_locals = ByteUtils.bytesToInt(max_locals_bytes, 0);
        System.out.printf("max_locals: %s (%d)\n", HexUtils.fromBytes(max_locals_bytes), max_locals);

        byte[] code_length_bytes = byteDashboard.nextN(4);
        int code_length = ByteUtils.bytesToInt(code_length_bytes, 0);
        System.out.printf("code_length: %s (%d)\n", HexUtils.fromBytes(code_length_bytes), code_length);

        byte[] code_bytes = byteDashboard.nextN(code_length);
        System.out.printf("code: %s\n", HexUtils.fromBytes(code_bytes));
        if(verbose) {
            List<String> lines = InstructionParser.bytes2Lines(code_bytes);
            for(String line : lines) {
                System.out.println(line);
            }
        }


        byte[] exception_table_length_bytes = byteDashboard.nextN(2);
        int exception_table_length = ByteUtils.bytesToInt(exception_table_length_bytes, 0);
        System.out.printf("exception_table_length: %s (%d)\n", HexUtils.fromBytes(exception_table_length_bytes), exception_table_length);
        for (int i = 0; i < exception_table_length; i++) {
            byte[] exception_info_bytes = byteDashboard.nextN(8);
            System.out.printf("exception_info: %s\n", HexUtils.fromBytes(exception_info_bytes));
        }

        byte[] attributes_count_bytes = byteDashboard.nextN(2);
        int attributes_count = ByteUtils.bytesToInt(attributes_count_bytes, 0);
        System.out.printf("attributes_count: %s (%d)\n", HexUtils.fromBytes(attributes_count_bytes), attributes_count);
        for (int i = 0; i < attributes_count; i++) {
            byte[] length_bytes = byteDashboard.peekN(2, 4);
            int length = ByteUtils.bytesToInt(length_bytes, 0);
            byte[] attr_bytes = byteDashboard.nextN(length + 6);
            System.out.printf("attribute_info: %s\n", HexUtils.fromBytes(attr_bytes));
        }
    }

    // endregion

    // region Code

    @Override
    public void visitLineNumberTable(LineNumberTable obj) {
        String name = obj.getName();
        String hexCode = obj.getHexCode();
        byte[] bytes = obj.getBytes();

        System.out.println(name);
        System.out.printf("HexCode: %s\n", hexCode);

        ByteDashboard byteDashboard = new ByteDashboard("LineNumberTable", bytes);
        int attribute_length = processAttributeHeader(byteDashboard);

        byte[] line_number_table_length_bytes = byteDashboard.nextN(2);
        int line_number_table_length = ByteUtils.bytesToInt(line_number_table_length_bytes, 0);
        System.out.printf("line_number_table_length: %s (%d)\n", HexUtils.fromBytes(line_number_table_length_bytes), line_number_table_length);

        if(line_number_table_length > 0) {
            System.out.println("line_number_table:");
            for (int i = 0; i < line_number_table_length; i++) {
                byte[] start_pc_bytes = byteDashboard.nextN(2);
                int start_pc = ByteUtils.bytesToInt(start_pc_bytes, 0);
                byte[] line_number_bytes = byteDashboard.nextN(2);
                int line_number = ByteUtils.bytesToInt(line_number_bytes, 0);
                System.out.printf("    start_pc: %s(%d) line_number: %s(%d)\n",
                        HexUtils.fromBytes(start_pc_bytes),
                        start_pc,
                        HexUtils.fromBytes(line_number_bytes),
                        line_number);
            }
        }
    }

    // endregion

    // region private methods
    private int processAttributeHeader(ByteDashboard byteDashboard) {
        byte[] attribute_name_index_bytes = byteDashboard.nextN(2);
        int attribute_name_index = ByteUtils.bytesToInt(attribute_name_index_bytes, 0);
        System.out.printf("attribute_name_index: %s (%d)\n", HexUtils.fromBytes(attribute_name_index_bytes), attribute_name_index);

        byte[] attribute_length_bytes = byteDashboard.nextN(4);
        int attribute_length = ByteUtils.bytesToInt(attribute_length_bytes, 0);
        System.out.printf("attribte_length: %s (%d)\n", HexUtils.fromBytes(attribute_length_bytes), attribute_length);
        return attribute_length;
    }
    // endregion
}
