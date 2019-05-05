package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public final class Code extends AttributeInfo {
    private final int max_stack;
    private final int max_locals;
    private final int code_length;
    private final byte[] code;
    private final int exception_table_length;
    private final List<ExceptionTable> exception_table_list;
    private final int attributes_count;
    private final Attributes attributes;

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
        this.attributes = new Attributes(byteDashboard, this.attributes_count, constantPool);
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

    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitCode(this);
    }
}
