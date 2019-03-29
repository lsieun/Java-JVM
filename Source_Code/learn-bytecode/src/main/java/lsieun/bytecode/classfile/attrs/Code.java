package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.basic.OpcodeConst;
import lsieun.bytecode.classfile.basic.TypeConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public final class Code extends AttributeInfo {
    private final int max_stack;
    private final int max_locals;
    private final int code_length;
    private final byte[] code;
    private final int exception_table_length;
    private final List<ExceptionTable> exception_table_list;
    private final int attributes_count;
    private final List<AttributeInfo> attributes_list;

    public Code(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        // 第一部分
        byte[] max_stack_bytes = byteDashboard.nextN(2);
        byte[] max_locals_bytes = byteDashboard.nextN(2);
        byte[] code_length_bytes = byteDashboard.nextN(4);

        this.max_stack = ByteUtils.bytesToInt(max_stack_bytes, 0);
        this.max_locals = ByteUtils.bytesToInt(max_locals_bytes, 0);
        this.code_length = ByteUtils.bytesToInt(code_length_bytes, 0);
        this.code = byteDashboard.nextN(code_length);

        // 第二部分
        byte[] exception_table_length_bytes = byteDashboard.nextN(2);
        this.exception_table_length = ByteUtils.bytesToInt(exception_table_length_bytes, 0);
        this.exception_table_list = new ArrayList();
        for(int i=0; i<exception_table_length; i++) {
            ExceptionTable item = new ExceptionTable(byteDashboard, constantPool);
            this.exception_table_list.add(item);
        }

        // 第三部分
        byte[] attributes_count_bytes = byteDashboard.nextN(2);
        this.attributes_count = ByteUtils.bytesToInt(attributes_count_bytes, 0);
        this.attributes_list = new ArrayList();
        for(int i=0; i<attributes_count; i++) {
            AttributeInfo item = AttributeInfo.read(byteDashboard, constantPool);
            this.attributes_list.add(item);
        }
    }

    public int getMaxStack() {
        return max_stack;
    }

    public int getMaxLocals() {
        return max_locals;
    }

    public int getCodeLength() {
        return code_length;
    }

    public byte[] getCode() {
        return code;
    }

    public int getExceptionTableLength() {
        return exception_table_length;
    }

    public List<ExceptionTable> getExceptionTableList() {
        return exception_table_list;
    }

    public int getAttributesCount() {
        return attributes_count;
    }

    public List<AttributeInfo> getAttributesList() {
        return attributes_list;
    }

    public List<String> getLines() {
        List<String> list = new ArrayList();
        list.add("maxStack='" + this.max_stack + "', maxLocals='" + this.max_locals + "'");
        list.add("codeLength='" + this.code_length + "'");
        list.add("code='" + HexUtils.fromBytes(this.code) + "'");
        List<String> codeLines = code2String(this.code);
        list.addAll(codeLines);
        // FIXME: 在这儿，处理Code代码
        list.add("");
        if(this.exception_table_list.size() > 0) {
            list.add("Exception table:");
            list.add("start_pc  end_pc  handler_pc  catch_type  Exception");
        }
        for(int i=0; i<this.exception_table_list.size(); i++) {
            ExceptionTable item = this.exception_table_list.get(i);
            list.add(String.format("%8d  %6d  %10d  %10d  ",
                    item.getStartPC(), item.getEndPC(), item.getHandlerPC(), item.getCatchType())
                    + item.getExceptionType()
            );
        }
        list.add("");

        for(int i=0; i<this.attributes_list.size(); i++) {
            AttributeInfo item = this.attributes_list.get(i);
            String name = item.getName();
            if("LineNumberTable".equals(name)) {
                LineNumberTable instance = (LineNumberTable) item;
                List<String> lines = instance.getLines();
                list.addAll(lines);
            }
            else if("LocalVariableTable".equals(name)) {
                LocalVariableTable instance = (LocalVariableTable) item;
                List<String> lines = instance.getLines();
                list.addAll(lines);
            }
            else if("LocalVariableTypeTable".equals(name)) {
                LocalVariableTypeTable instance = (LocalVariableTypeTable) item;
                List<String> lines = instance.getLines();
                list.addAll(lines);
            }
            list.add("");
        }
        return list;
    }

    public static List<String> code2String(byte[] code) {
        ByteDashboard board = new ByteDashboard("MethodCode", code);

        List<String> list = new ArrayList();

        while (board.hasNex()) {
            int index = board.getIndex();

            byte b = board.next();
            int opcode = b & 0xFF;
            String opcodeName = OpcodeConst.getOpcodeName(opcode);


            String operandStr = "";
            short num = OpcodeConst.getNoOfOperands(opcode);
            byte[] bytes = board.nextN(num);

            long count = OpcodeConst.getOperandTypeCount(opcode);
            int byteIndex = 0;
            for(int i=0; i<count; i++) {

                short operandType = OpcodeConst.getOperandType(opcode, i);
                byte[] operand_value_bytes;
                int operandValue = 0;
                switch (operandType) {
                    case TypeConst.T_BYTE:
                        operand_value_bytes = new byte[1];
                        operand_value_bytes[0] = bytes[byteIndex];
                        operandValue = ByteUtils.bytesToInt(operand_value_bytes, 0);
                        byteIndex += 1;
                        break;
                    case TypeConst.T_SHORT:
                        operand_value_bytes = new byte[2];
                        operand_value_bytes[0] = bytes[byteIndex + 0];
                        operand_value_bytes[1] = bytes[byteIndex + 1];
                        operandValue = ByteUtils.bytesToInt(operand_value_bytes, 0);
                        byteIndex += 2;
                        break;
                    case TypeConst.T_INT:
                        operand_value_bytes = new byte[4];
                        operand_value_bytes[0] = bytes[byteIndex + 0];
                        operand_value_bytes[1] = bytes[byteIndex + 1];
                        operand_value_bytes[2] = bytes[byteIndex + 2];
                        operand_value_bytes[3] = bytes[byteIndex + 3];
                        operandValue = ByteUtils.bytesToInt(operand_value_bytes, 0);
                        byteIndex += 4;
                        break;
                    default:
                        for(String line : list) {
                            System.out.println(line);
                        }
                        System.out.println("opcode: " + opcodeName);
                        System.out.println("NoOfOperands: " + HexUtils.fromBytes(bytes));
                        System.out.println("OperandTypeCount: " + count);
                        break;
                }
                operandStr += " " + operandValue;
            }

            list.add(String.format("%5d: ", index) + opcodeName + operandStr);
        }

        return list;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        for(String line : getLines()) {
            list.add("    " + line + StringUtils.LF);
        }
//        list.add("    maxStack='" + this.max_stack + "', maxLocals='" + this.max_locals + "'" + StringUtils.LF);
//        list.add("    codeLength='" + this.code_length + "'" + StringUtils.LF);
//        list.add("    code='" + HexUtils.fromBytes(this.code) + "'" + StringUtils.LF);
//        list.add(StringUtils.LF);

//        List<String> exceptionTableList = new ArrayList();
//        for(int i=0; i<this.exception_table_list.size(); i++) {
//            ExceptionTable item = this.exception_table_list.get(i);
//            exceptionTableList.add(item.getName());
//        }
//        list.add("Value='" + StringUtils.list2str(innerClassList, "[","]",", ") + "'");
//        list.add("HexCode='" + super.getHexCode() + "'");

        list.add("    Attributes:" + StringUtils.LF);
        for(int i=0; i<this.attributes_list.size(); i++) {
            AttributeInfo item = this.attributes_list.get(i);
            String name = item.getName();
            list.add("    " + item.toString() + StringUtils.LF);
        }

        String content = StringUtils.list2str(list, "");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {" + StringUtils.LF);
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
