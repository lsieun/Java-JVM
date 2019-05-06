package lsieun.bytecode.classfile.visitors;

import java.util.Collections;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.attrs.method.ExceptionTable;
import lsieun.bytecode.classfile.attrs.code.LineNumber;
import lsieun.bytecode.classfile.attrs.code.LineNumberTable;
import lsieun.bytecode.classfile.attrs.code.LocalVariable;
import lsieun.bytecode.classfile.attrs.code.LocalVariableTable;
import lsieun.bytecode.utils.InstructionParser;
import lsieun.utils.radix.HexUtils;

public class MethodStandardVisitor extends AbstractVisitor {
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

        methods.accept(this);
    }

    @Override
    public void visitMethods(Methods obj) {
        MethodInfo[] entries = obj.getEntries();
        if(entries != null && entries.length > 0) {
            System.out.println("\nAvailable Methods:");
            for(MethodInfo item : entries) {
                Attributes attributes = item.getAttributes();
                String attrNames = attributes.getAttributesName();

                String line = String.format("    Method='%s', AccessFlags='%s', Attrs='%s'",
                        item.getValue(),
                        item.getAccessFlagsString(),
                        attrNames);
                System.out.println(line);
            }
        }

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

        attributes.accept(this);
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
        List<String> codeLines = InstructionParser.bytes2Lines(bytes);
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
        AttributeInfo localVariableTable = attributes.findAttribute("LocalVariableTable");
        if(localVariableTable != null) {
            localVariableTable.accept(this);
        }

        AttributeInfo lineNumberTable = attributes.findAttribute("LineNumberTable");
        if(lineNumberTable != null) {
            lineNumberTable.accept(this);
        }

        AttributeInfo stackMapTable = attributes.findAttribute("StackMapTable");
        if(stackMapTable != null) {
            stackMapTable.accept(this);
        }

        String attributesName = attributes.getAttributesName();
        System.out.println("\nattributes: " + attributesName);

        System.out.println("}");
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
    // endregion
}
