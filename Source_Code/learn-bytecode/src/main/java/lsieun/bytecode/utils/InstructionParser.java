package lsieun.bytecode.utils;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.generic.cnst.OpcodeConst;
import lsieun.bytecode.generic.cnst.TypeConst;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public class InstructionParser {

    public static List<String> bytes2Lines(byte[] code) {
        ByteDashboard board = new ByteDashboard("MethodCode", code);

        List<String> list = new ArrayList();

        while (board.hasNex()) {
            int index = board.getIndex();
            List<Byte> byteList = new ArrayList();

            byte b = board.next();
            int opcode = b & 0xFF;
            String opcodeName = OpcodeConst.getOpcodeName(opcode);
            byteList.add(b);

            if(opcode == 170) {
                //tableswitch
                int ceiling = getCeiling(index+1, 4);
                int pad_num = ceiling - (index + 1);
                // pad
                if(pad_num > 0) {
                    byte[] padBytes = board.nextN(pad_num);
                    addArray2List(byteList, padBytes);
                }
                //
                byte[] defaultByte_bytes = board.nextN(4);
                byte[] lowByte_bytes = board.nextN(4);
                byte[] highByte_bytes = board.nextN(4);

                addArray2List(byteList, defaultByte_bytes);
                addArray2List(byteList, lowByte_bytes);
                addArray2List(byteList, highByte_bytes);

                int defaultByte = ByteUtils.bytesToInt(defaultByte_bytes, 0);
                int lowByte = ByteUtils.bytesToInt(lowByte_bytes, 0);
                int highByte = ByteUtils.bytesToInt(highByte_bytes, 0);
                String jumpStr = "[" + pad_num + "] " + defaultByte + " " + lowByte + "-" + highByte + ": ";
                List<String> offsetList = new ArrayList();

                for(int i=lowByte; i<=highByte; i++) {
                    byte[] offset_bytes = board.nextN(4);
                    int offset = ByteUtils.bytesToInt(offset_bytes, 0);
                    addArray2List(byteList, offset_bytes);
                    offsetList.add(String.valueOf(offset));
                }
                jumpStr += StringUtils.list2str(offsetList, ",");

                list.add(String.format("%5d: %-16s", index, (opcodeName + jumpStr)) + "// " + HexUtils.fromBytes(byteList));
                continue;
            }
            else if(opcode == 171) {
                //lookupswitch
                int ceiling = getCeiling(index+1, 4);
                int pad_num = ceiling - (index + 1);
                // pad
                if(pad_num > 0) {
                    byte[] padBytes = board.nextN(pad_num);
                    addArray2List(byteList, padBytes);
                }
                //
                byte[] defaultByte_bytes = board.nextN(4);
                byte[] npairs_bytes = board.nextN(4);

                addArray2List(byteList, defaultByte_bytes);
                addArray2List(byteList, npairs_bytes);

                int defaultByte = ByteUtils.bytesToInt(defaultByte_bytes, 0);
                int npairs = ByteUtils.bytesToInt(npairs_bytes, 0);

                String jumpStr = "[" + pad_num + "] " + defaultByte + " " + npairs + ": ";
                List<String> offsetPairList = new ArrayList();
                for(int i=0; i<npairs; i++) {
                    byte[] caseValue_bytes = board.nextN(4);
                    byte[] offset_bytes = board.nextN(4);
                    int caseValue = ByteUtils.bytesToInt(caseValue_bytes, 0);
                    int offset = ByteUtils.bytesToInt(offset_bytes, 0);

                    addArray2List(byteList, caseValue_bytes);
                    addArray2List(byteList, offset_bytes);
                    offsetPairList.add(caseValue + ":" + offset);
                }
                jumpStr += StringUtils.list2str(offsetPairList, ",");

                list.add(String.format("%5d: %-16s", index, (opcodeName + jumpStr)) + "// " + HexUtils.fromBytes(byteList));

                continue;
            }

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
                        byteList.add(operand_value_bytes[0]);
                        break;
                    case TypeConst.T_SHORT:
                        operand_value_bytes = new byte[2];
                        operand_value_bytes[0] = bytes[byteIndex + 0];
                        operand_value_bytes[1] = bytes[byteIndex + 1];
                        operandValue = ByteUtils.bytesToInt(operand_value_bytes, 0);
                        byteIndex += 2;
                        byteList.add(operand_value_bytes[0]);
                        byteList.add(operand_value_bytes[1]);
                        break;
                    case TypeConst.T_INT:
                        operand_value_bytes = new byte[4];
                        operand_value_bytes[0] = bytes[byteIndex + 0];
                        operand_value_bytes[1] = bytes[byteIndex + 1];
                        operand_value_bytes[2] = bytes[byteIndex + 2];
                        operand_value_bytes[3] = bytes[byteIndex + 3];
                        operandValue = ByteUtils.bytesToInt(operand_value_bytes, 0);
                        byteIndex += 4;
                        byteList.add(operand_value_bytes[0]);
                        byteList.add(operand_value_bytes[1]);
                        byteList.add(operand_value_bytes[2]);
                        byteList.add(operand_value_bytes[3]);

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

            list.add(String.format("%5d: %-16s", index, (opcodeName + operandStr)) + "// " + HexUtils.fromBytes(byteList));
        }

        return list;
    }

    private static void addArray2List(List<Byte> list, byte[] bytes) {
        if(list == null) return;
        if(bytes == null || bytes.length < 1) return;
        for(int i=0; i<bytes.length; i++) {
            byte b = bytes[i];
            list.add(b);
        }
    }

    private static int getCeiling(int index, int base) {
        int quotient = index / base;
        int remainder = index % base;
        if(remainder == 0) return index;
        int newIndex = (quotient + 1) * base;
        return newIndex;
    }

}
