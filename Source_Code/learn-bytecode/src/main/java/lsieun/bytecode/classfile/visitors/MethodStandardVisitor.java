package lsieun.bytecode.classfile.visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lsieun.bytecode.classfile.AccessFlags;
import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.AttributesCount;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.ConstantPoolCount;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
import lsieun.bytecode.classfile.FieldsCount;
import lsieun.bytecode.classfile.Interfaces;
import lsieun.bytecode.classfile.InterfacesCount;
import lsieun.bytecode.classfile.MagicNumber;
import lsieun.bytecode.classfile.MajorVersion;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.MethodsCount;
import lsieun.bytecode.classfile.MinorVersion;
import lsieun.bytecode.classfile.SuperClass;
import lsieun.bytecode.classfile.ThisClass;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.attrs.Code;
import lsieun.bytecode.classfile.attrs.ConstantValue;
import lsieun.bytecode.classfile.attrs.Deprecated;
import lsieun.bytecode.classfile.attrs.ExceptionTable;
import lsieun.bytecode.classfile.attrs.Exceptions;
import lsieun.bytecode.classfile.attrs.InnerClasses;
import lsieun.bytecode.classfile.attrs.LineNumber;
import lsieun.bytecode.classfile.attrs.LineNumberTable;
import lsieun.bytecode.classfile.attrs.LocalVariable;
import lsieun.bytecode.classfile.attrs.LocalVariableTable;
import lsieun.bytecode.classfile.attrs.LocalVariableTypeTable;
import lsieun.bytecode.classfile.attrs.RuntimeVisibleAnnotations;
import lsieun.bytecode.classfile.attrs.Signature;
import lsieun.bytecode.classfile.attrs.SourceFile;
import lsieun.bytecode.classfile.attrs.StackMapTable;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.classfile.basic.OpcodeConst;
import lsieun.bytecode.classfile.basic.TypeConst;
import lsieun.bytecode.classfile.cp.Constant;
import lsieun.bytecode.classfile.cp.ConstantClass;
import lsieun.bytecode.classfile.cp.ConstantDouble;
import lsieun.bytecode.classfile.cp.ConstantDynamic;
import lsieun.bytecode.classfile.cp.ConstantFieldref;
import lsieun.bytecode.classfile.cp.ConstantFloat;
import lsieun.bytecode.classfile.cp.ConstantInteger;
import lsieun.bytecode.classfile.cp.ConstantInterfaceMethodref;
import lsieun.bytecode.classfile.cp.ConstantInvokeDynamic;
import lsieun.bytecode.classfile.cp.ConstantLong;
import lsieun.bytecode.classfile.cp.ConstantMethodHandle;
import lsieun.bytecode.classfile.cp.ConstantMethodType;
import lsieun.bytecode.classfile.cp.ConstantMethodref;
import lsieun.bytecode.classfile.cp.ConstantModule;
import lsieun.bytecode.classfile.cp.ConstantNameAndType;
import lsieun.bytecode.classfile.cp.ConstantPackage;
import lsieun.bytecode.classfile.cp.ConstantString;
import lsieun.bytecode.classfile.cp.ConstantUtf8;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;
import lsieun.utils.radix.HexUtils;

public class MethodStandardVisitor implements Visitor {
    private String nameAndType;

    public MethodStandardVisitor(String nameAndType) {
        this.nameAndType = nameAndType;
    }

    @Override
    public void visitClassFile(ClassFile obj) {
        ConstantPool constantPool = obj.getConstantPool();
        constantPool.accept(this);

        Methods methods = obj.getMethods();
        MethodInfo methodInfo = methods.findByNameAndType(nameAndType);
        if(methodInfo != null) {
            methodInfo.accept(this);
        }
        else {
            //
        }
    }

    @Override
    public void visitMagicNumber(MagicNumber obj) {

    }

    @Override
    public void visitMinorVersion(MinorVersion obj) {

    }

    @Override
    public void visitMajorVersion(MajorVersion obj) {

    }

    @Override
    public void visitConstantPoolCount(ConstantPoolCount obj) {

    }

    @Override
    public void visitConstantPool(ConstantPool obj) {
        System.out.println("\nConstantPool {");
        for(int i=0; i<obj.getEntries().length; i++) {
            Constant item = obj.getEntries()[i];
            if(item == null) continue;
            item.accept(this);
        }
        System.out.println("}");
    }

    @Override
    public void visitAccessFlags(AccessFlags obj) {

    }

    @Override
    public void visitThisClass(ThisClass obj) {

    }

    @Override
    public void visitSuperClass(SuperClass obj) {

    }

    @Override
    public void visitInterfacesCount(InterfacesCount obj) {

    }

    @Override
    public void visitInterfaces(Interfaces obj) {

    }

    @Override
    public void visitFieldsCount(FieldsCount obj) {

    }

    @Override
    public void visitFields(Fields obj) {

    }

    @Override
    public void visitFieldInfo(FieldInfo obj) {

    }

    @Override
    public void visitMethodsCount(MethodsCount obj) {

    }

    @Override
    public void visitMethods(Methods obj) {

    }

    @Override
    public void visitMethodInfo(MethodInfo obj) {
        Attributes attributes = obj.getAttributes();
        String attrNames = attributes.getAttributesName();

        String line = String.format("\nMethodInfo {\n    MethodName='%s'\n    AccessFlags='%s'\n    Attrs='%s'\n}",
                obj.getValue(),
                obj.getAccessFlagsString(),
                attrNames);
        System.out.println(line);

        AttributeInfo[] entries = attributes.getEntries();
        if(entries != null && entries.length > 0) {
            for(AttributeInfo item : entries) {
                item.accept(this);
            }
        }
    }


    @Override
    public void visitAttributesCount(AttributesCount obj) {

    }

    @Override
    public void visitAttributes(Attributes obj) {
        AttributeInfo localVariableTable = obj.findAttribute("LocalVariableTable");
        if(localVariableTable != null) {
            localVariableTable.accept(this);
        }

        AttributeInfo lineNumberTable = obj.findAttribute("LineNumberTable");
        if(lineNumberTable != null) {
            lineNumberTable.accept(this);
        }

        AttributeInfo stackMapTable = obj.findAttribute("StackMapTable");
        if(stackMapTable != null) {
            stackMapTable.accept(this);
        }

        String attributesName = obj.getAttributesName();
        System.out.println("\nattributes: " + attributesName);
    }

    @Override
    public void visitAttributeInfo(AttributeInfo obj) {
        String line = String.format("\n%s {attribute_name_index='%s', attribute_length='%d', HexCode='%s'}",
                obj.getName(),
                obj.getAttributeNameIndex(),
                obj.getAttributeLength(),
                obj.getHexCode());
        System.out.println(line);
    }

    // region constant pool
    @Override
    public void visitConstant(Constant obj) {
        String line = String.format("    |%03d| %s {Value='%s', HexCode='%s'}",
                obj.getIndex(),
                CPConst.getConstantName(obj.getTag()),
                obj.getValue(),
                obj.getHexCode());
        System.out.println(line);
    }

    @Override
    public void visitConstantUtf8(ConstantUtf8 obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantInteger(ConstantInteger obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantFloat(ConstantFloat obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantLong(ConstantLong obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantDouble(ConstantDouble obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantClass(ConstantClass obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantString(ConstantString obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantFieldref(ConstantFieldref obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantMethodref(ConstantMethodref obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantNameAndType(ConstantNameAndType obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantMethodHandle(ConstantMethodHandle obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantMethodType(ConstantMethodType obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantDynamic(ConstantDynamic obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantInvokeDynamic(ConstantInvokeDynamic obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantModule(ConstantModule obj) {
        visitConstant(obj);
    }

    @Override
    public void visitConstantPackage(ConstantPackage obj) {
        visitConstant(obj);
    }
    // endregion

    // region attributes
    @Override
    public void visitCode(Code obj) {
        int maxStack = obj.getMaxStack();
        int maxLocals = obj.getMaxLocals();
        int codeLength = obj.getCodeLength();
        byte[] bytes = obj.getCode();


        String line = String.format("\nCode {\nmax_stack='%d', max_locals='%d'\ncode_length='%d'\ncode='%s'",
                maxStack, maxLocals, codeLength, HexUtils.fromBytes(bytes));
        System.out.println(line);

        // instruction
        List<String> codeLines = code2String(bytes);
        for(String instruction : codeLines) {
            System.out.println(instruction);
        }

        // exception table
        List<ExceptionTable> exceptionTableList = obj.getExceptionTableList();
        if(exceptionTableList.size() > 0) {
            System.out.println("\nException table:");
            System.out.println("start_pc  end_pc  handler_pc  catch_type  Exception");
            for(int i=0; i<exceptionTableList.size(); i++) {
                ExceptionTable item = exceptionTableList.get(i);
                String exception_line = String.format("%8d  %6d  %10d  %10d  %s",
                        item.getStartPC(),
                        item.getEndPC(),
                        item.getHandlerPC(),
                        item.getCatchType(),
                        item.getExceptionType());
                System.out.println(exception_line);
            }
        }

        Attributes attributes = obj.getAttributes();
        attributes.accept(this);

        System.out.println("}");
    }

    public static List<String> code2String(byte[] code) {
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


    @Override
    public void visitConstantValue(ConstantValue obj) {

    }

    @Override
    public void visitDeprecated(Deprecated obj) {

    }

    @Override
    public void visitExceptions(Exceptions obj) {

    }

    @Override
    public void visitInnerClasses(InnerClasses obj) {

    }

    @Override
    public void visitLineNumberTable(LineNumberTable obj) {
        List<LineNumber> lineNumberList = obj.getLineNumberList();
        if(lineNumberList.size() > 0) {
            System.out.println("\nLineNumberTable:");
            System.out.println("start_pc  line_number");
            for(int i=0; i<lineNumberList.size(); i++) {
                LineNumber item = lineNumberList.get(i);
                String line = String.format("%8d  %11d", item.getStartPC(), item.getLineNumber());
                System.out.println(line);
            }
        }

    }

    @Override
    public void visitLocalVariableTable(LocalVariableTable obj) {
        List<LocalVariable> localVariableList = obj.getLocalVariableList();
        Collections.sort(localVariableList);
        if(localVariableList.size() > 0) {
            System.out.println("\nLocalVariableTable:");
            System.out.println("index  start_pc  length  name_and_type");
            for(int i=0; i<localVariableList.size(); i++) {
                LocalVariable item = localVariableList.get(i);
                String line = String.format("%5d  %8d  %6d  %s",
                        item.getIndex(),
                        item.getStartPC(),
                        item.getLength(),
                        item.getNameAndType());
                System.out.println(line);
            }
        }

    }

    @Override
    public void visitLocalVariableTypeTable(LocalVariableTypeTable obj) {

    }

    @Override
    public void visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations obj) {

    }

    @Override
    public void visitSignature(Signature obj) {

    }

    @Override
    public void visitSourceFile(SourceFile obj) {

    }

    @Override
    public void visitStackMapTable(StackMapTable obj) {

    }
    // endregion
}
