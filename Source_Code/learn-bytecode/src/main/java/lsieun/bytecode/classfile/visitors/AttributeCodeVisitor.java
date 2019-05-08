package lsieun.bytecode.classfile.visitors;

import lsieun.bytecode.classfile.attrs.code.LineNumberTable;
import lsieun.bytecode.classfile.attrs.code.StackMapTable;
import lsieun.bytecode.classfile.basic.StackMapConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public class AttributeCodeVisitor extends AttributeVisitor {

    // region Code

    @Override
    public void visitLineNumberTable(LineNumberTable obj) {
        String name = obj.getName();
        String hexCode = obj.getHexCode();
        byte[] bytes = obj.getBytes();

        System.out.println(name);
        System.out.printf("HexCode='%s'\n", hexCode);

        ByteDashboard byteDashboard = new ByteDashboard("LineNumberTable", bytes);
        int attribute_length = processAttributeHeader(byteDashboard);

        byte[] line_number_table_length_bytes = byteDashboard.nextN(2);
        int line_number_table_length = ByteUtils.bytesToInt(line_number_table_length_bytes, 0);
        System.out.printf("line_number_table_length='0x%s' (%d)\n", HexUtils.fromBytes(line_number_table_length_bytes), line_number_table_length);

        if(line_number_table_length > 0) {
            System.out.println("line_number_table:");
            for (int i = 0; i < line_number_table_length; i++) {
                byte[] start_pc_bytes = byteDashboard.nextN(2);
                int start_pc = ByteUtils.bytesToInt(start_pc_bytes, 0);
                byte[] line_number_bytes = byteDashboard.nextN(2);
                int line_number = ByteUtils.bytesToInt(line_number_bytes, 0);
                System.out.printf("    start_pc='0x%s'(%d) line_number='0x%s'(%d)\n",
                        HexUtils.fromBytes(start_pc_bytes),
                        start_pc,
                        HexUtils.fromBytes(line_number_bytes),
                        line_number);
            }
        }
    }

    @Override
    public void visitStackMapTable(StackMapTable obj) {
        String name = obj.getName();
        byte[] bytes = obj.getBytes();
        String hexCode = obj.getHexCode();

        System.out.println(name);
        System.out.printf("HexCode='%s'\n", hexCode);

        ByteDashboard byteDashboard = new ByteDashboard("StackMapTable", bytes);
        int attribute_length = processAttributeHeader(byteDashboard);

        byte[] number_of_entries_bytes = byteDashboard.nextN(2);
        int number_of_entries = ByteUtils.bytesToInt(number_of_entries_bytes, 0);
        System.out.printf("number_of_entries='0x%s' (%d)\n", HexUtils.fromBytes(number_of_entries_bytes), number_of_entries);

        for(int i=0; i<number_of_entries; i++) {
            displayStackMapFrame(byteDashboard);
        }
    }

    private void displayStackMapFrame(ByteDashboard byteDashboard) {
        byte[] frame_type_bytes = byteDashboard.nextN(1);
        int frame_type = ByteUtils.bytesToInt(frame_type_bytes, 0);
        String frame_type_hex = HexUtils.fromBytes(frame_type_bytes);

        if (frame_type >= StackMapConst.SAME_FRAME && frame_type <= StackMapConst.SAME_FRAME_MAX) {
            String format = "\n%s {\n    frame_type='0x%s'(%d)\n    offset_delta='%d'\n}";
            String line = String.format(format, "SAME", frame_type_hex, frame_type, frame_type);
            System.out.println(line);
        } else if (frame_type >= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME &&
                frame_type <= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            String format = "\n%s {\n    frame_type='0x%s'(%d)\n    offset_delta='%d'";
            String line = String.format(format, "SAME_LOCALS_1_STACK", frame_type_hex, frame_type, (frame_type-64));
            System.out.println(line);
            displayStackMapType(byteDashboard);
            System.out.println("}");
        } else if (frame_type == StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            byte[] offset_bytes = byteDashboard.nextN(2);
            String offset_hex = HexUtils.fromBytes(offset_bytes);
            int offset = ByteUtils.bytesToInt(offset_bytes, 0);

            String format = "\n%s {\n    frame_type='0x%s'(%d)\n    offset_delta='%s'(%d)";
            String line = String.format(format, "SAME_LOCALS_1_STACK_EXTENDED",
                    frame_type_hex, frame_type, (frame_type-64), offset_hex, offset);
            System.out.println(line);
            displayStackMapType(byteDashboard);
            System.out.println("}");
        } else if (frame_type >= StackMapConst.CHOP_FRAME && frame_type <= StackMapConst.CHOP_FRAME_MAX) {
            byte[] offset_bytes = byteDashboard.nextN(2);
            int offset = ByteUtils.bytesToInt(offset_bytes, 0);
            String offset_hex = HexUtils.fromBytes(offset_bytes);

            System.out.printf("\n%s {\n", "CHOP");
            System.out.printf("    frame_type='0x%s'(%d) last_k=%d\n", frame_type_hex, frame_type, (251-frame_type));
            System.out.printf("    offset_delta=%d\n", offset_hex, offset);
            System.out.println("}");
        } else if (frame_type == StackMapConst.SAME_FRAME_EXTENDED) {
            byte[] offset_bytes = byteDashboard.nextN(2);
            int offset = ByteUtils.bytesToInt(offset_bytes, 0);
            String offset_hex = HexUtils.fromBytes(offset_bytes);

            System.out.printf("\n%s {\n", "SAME_EXTENDED");
            System.out.printf("    frame_type='0x%s'(%d)\n", frame_type_hex, frame_type);
            System.out.printf("    offset_delta=%d\n", offset_hex, offset);
            System.out.println("}");
        } else if (frame_type >= StackMapConst.APPEND_FRAME && frame_type <= StackMapConst.APPEND_FRAME_MAX) {
            int last_k = (frame_type-251);
            byte[] offset_bytes = byteDashboard.nextN(2);
            int offset = ByteUtils.bytesToInt(offset_bytes, 0);
            String offset_hex = HexUtils.fromBytes(offset_bytes);

            System.out.printf("\n%s {\n", "APPEND");
            System.out.printf("    frame_type='0x%s'(%d)\n", frame_type_hex, frame_type);
            System.out.printf("    offset_delta='0x%s'(%d)\n", offset_hex, offset);
            System.out.printf("    last_k=%d\n", last_k);
            for(int i=0; i<last_k; i++) {
                displayStackMapType(byteDashboard);
            }
            System.out.println("}");
        } else if (frame_type == StackMapConst.FULL_FRAME) {
            byte[] offset_bytes = byteDashboard.nextN(2);
            int offset = ByteUtils.bytesToInt(offset_bytes, 0);
            String offset_hex = HexUtils.fromBytes(offset_bytes);

            System.out.printf("\n%s {\n", "FULL");
            System.out.printf("    frame_type='0x%s'(%d)\n", frame_type_hex, frame_type);
            System.out.printf("    offset_delta='0x%s'(%d)\n", offset_hex, offset);

            byte[] number_of_locals_bytes = byteDashboard.nextN(2);
            int number_of_locals = ByteUtils.bytesToInt(number_of_locals_bytes, 0);
            String number_of_locals_hex = HexUtils.fromBytes(number_of_locals_bytes);
            System.out.printf("    number_of_locals='0x%s'(%d)\n", number_of_locals_hex, number_of_locals);

            for(int i=0; i<number_of_locals; i++) {
                displayStackMapType(byteDashboard);
            }

            byte[] number_of_stack_items_bytes = byteDashboard.nextN(2);
            int number_of_stack_items = ByteUtils.bytesToInt(number_of_stack_items_bytes, 0);
            String number_of_stack_items_hex = HexUtils.fromBytes(number_of_stack_items_bytes);
            System.out.printf("    number_of_stack_items='0x%s'(%d)\n", number_of_stack_items_hex, number_of_stack_items);

            for(int i=0; i<number_of_stack_items; i++) {
                displayStackMapType(byteDashboard);
            }

            System.out.println("}");
        } else {
            String format = "\n%s {\n    frame_type='0x%s'(%d)\n}";
            String line = String.format(format, "UNKNOWN", frame_type_hex, frame_type);
            System.out.println(line);
            System.out.println("}");
        }
    }

    private void displayStackMapType(ByteDashboard byteDashboard) {
        byte[] tag_bytes = byteDashboard.nextN(1);
        int tag = ByteUtils.bytesToInt(tag_bytes, 0);
        String tag_hex = HexUtils.fromBytes(tag_bytes);
        String tagName = StackMapConst.getItemRawName(tag);

        String format = "    %s {tag='0x%s'}";
        String newFormat = "    %s {tag='0x%s', second_part='0x%s'(%d)}";

        if(tag == StackMapConst.ITEM_Bogus ||
                tag == StackMapConst.ITEM_Integer || tag == StackMapConst.ITEM_Float ||
                tag == StackMapConst.ITEM_Double || tag == StackMapConst.ITEM_Long ||
                tag == StackMapConst.ITEM_Null || tag == StackMapConst.ITEM_InitObject
        ) {
            String line = String.format(format, tagName, tag_hex);
            System.out.println(line);
        }
        else if(tag == StackMapConst.ITEM_Object) {
            byte[] cpool_index_bytes = byteDashboard.nextN(2);
            int cpool_index = ByteUtils.bytesToInt(cpool_index_bytes, 0);
            String cpool_index_hex = HexUtils.fromBytes(cpool_index_bytes);

            String line = String.format(newFormat, tagName, tag_hex, cpool_index_hex, cpool_index);
            String value = line.replace("second_part", "cpool_index");
            System.out.println(value);
        }
        else if(tag == StackMapConst.ITEM_NewObject) {
            byte[] offset_bytes = byteDashboard.nextN(2);
            String offset_hex = HexUtils.fromBytes(offset_bytes);
            int offset = ByteUtils.bytesToInt(offset_bytes, 0);

            String line = String.format(newFormat, tagName, tag_hex, offset_hex, offset);
            String value = line.replace("second_part", "offset");
            System.out.println(value);
        }
        else {
            String line = String.format(format, "UNKNOWN", tag_hex);
            System.out.println(line);
        }
    }

    // endregion
}
