package lsieun.bytecode.classfile.visitors;

import java.util.Collections;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.attrs.code.StackMapFrame;
import lsieun.bytecode.classfile.attrs.code.StackMapTable;
import lsieun.bytecode.classfile.attrs.code.StackMapType;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.attrs.method.ExceptionTable;
import lsieun.bytecode.classfile.attrs.code.LineNumber;
import lsieun.bytecode.classfile.attrs.code.LineNumberTable;
import lsieun.bytecode.classfile.attrs.code.LocalVariable;
import lsieun.bytecode.classfile.attrs.code.LocalVariableTable;
import lsieun.bytecode.generic.cst.StackMapConst;
import lsieun.bytecode.utils.InstructionParser;
import lsieun.bytecode.utils.clazz.AttributeUtils;
import lsieun.bytecode.utils.clazz.MethodUtils;
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
        MethodInfo methodInfo = MethodUtils.findMethod(methods, nameAndType);
        if(methodInfo != null) {
            methodInfo.accept(this);
        }

        methods.accept(this);
    }

    @Override
    public void visitMethods(Methods obj) {


    }

    @Override
    public void visitMethodInfo(MethodInfo obj) {
        Attributes attributes = obj.getAttributes();
        String attrNames = AttributeUtils.getAttributeNames(attributes);

        String line = String.format("\nMethodInfo {\n    MethodName='%s'\n    AccessFlags='%s'\n    Attrs='%s'\n}",
                obj.getValue(),
                obj.getAccessFlagsString(),
                attrNames);
        System.out.println(line);

        attributes.accept(this);
    }

    // region attributes
    @Override
    public void visitCode(Code obj) {
        int maxStack = obj.getMaxStack();
        int maxLocals = obj.getMaxLocals();
        int codeLength = obj.getCodeLength();
        byte[] bytes = obj.getCode();
        Attributes attributes = obj.getAttributes();

        System.out.printf("Code {\n");
        System.out.printf("max_stack='%d', max_locals='%d'\n", maxStack, maxLocals);
        System.out.printf("code_length='%d'\n", codeLength);
        String attributesName = AttributeUtils.getAttributeNames(attributes);
        System.out.printf("attributes='%s'\n\n", attributesName);
        System.out.printf("code='%s'\n", HexUtils.fromBytes(bytes));

        // instruction
        List<String> codeLines = InstructionParser.bytes2Lines(bytes);
        if(codeLines != null && codeLines.size() > 0) {

            for(String instruction : codeLines) {
                System.out.println(instruction);
            }
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


        AttributeInfo localVariableTable = AttributeUtils.findAttribute(attributes, "LocalVariableTable");
        if(localVariableTable != null) {
            localVariableTable.accept(this);
        }

        AttributeInfo lineNumberTable = AttributeUtils.findAttribute(attributes, "LineNumberTable");
        if(lineNumberTable != null) {
            lineNumberTable.accept(this);
        }

        AttributeInfo stackMapTable = AttributeUtils.findAttribute(attributes, "StackMapTable");
        if(stackMapTable != null) {
            stackMapTable.accept(this);
        }


        System.out.println("}\n");
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
    public void visitStackMapTable(StackMapTable obj) {
        String name = obj.getName();
        StackMapFrame[] entries = obj.getEntries();

        if(entries != null && entries.length > 0) {
            System.out.println("\nStackMapTable:");
            for(StackMapFrame entry : entries) {
                int frame_type = entry.getFrameType();
                int byte_code_offset = entry.getByteCodeOffset();
                StackMapType[] types_of_locals = entry.getTypesOfLocals();
                StackMapType[] types_of_stack_items = entry.getTypesOfStackItems();

                final StringBuilder buf = new StringBuilder(64);
                buf.append("    ");
                if (frame_type >= StackMapConst.SAME_FRAME && frame_type <= StackMapConst.SAME_FRAME_MAX) {
                    buf.append("SAME");
                } else if (frame_type >= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME &&
                        frame_type <= StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
                    buf.append("SAME_LOCALS_1_STACK");
                } else if (frame_type == StackMapConst.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
                    buf.append("SAME_LOCALS_1_STACK_EXTENDED");
                } else if (frame_type >= StackMapConst.CHOP_FRAME && frame_type <= StackMapConst.CHOP_FRAME_MAX) {
                    buf.append("CHOP ").append(String.valueOf(251-frame_type));
                } else if (frame_type == StackMapConst.SAME_FRAME_EXTENDED) {
                    buf.append("SAME_EXTENDED");
                } else if (frame_type >= StackMapConst.APPEND_FRAME && frame_type <= StackMapConst.APPEND_FRAME_MAX) {
                    buf.append("APPEND ").append(String.valueOf(frame_type-251));
                } else if (frame_type == StackMapConst.FULL_FRAME) {
                    buf.append("FULL");
                } else {
                    buf.append("UNKNOWN (").append(frame_type).append(")");
                }
                buf.append(" {offset_delta=").append(byte_code_offset);
                if (types_of_locals.length > 0) {
                    buf.append(", " + entry.getLocalsString());
                }
                if (types_of_stack_items.length > 0) {
                    buf.append(", " + entry.getStackItemsString());
                }
                buf.append("}");
                System.out.println(buf);
            }
        }
    }

    // endregion
}
