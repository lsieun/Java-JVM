package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.Visitor;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;
import lsieun.utils.radix.ByteUtils;

public final class Exceptions extends AttributeInfo {
    private final int number_of_exceptions;
    private final List<Integer> exception_index_list;
    private final String value;


    public Exceptions(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] number_of_exceptions_bytes = byteDashboard.nextN(2);
        this.number_of_exceptions = ByteUtils.bytesToInt(number_of_exceptions_bytes, 0);
        this.exception_index_list = new ArrayList();
        List<String> list = new ArrayList();
        for(int i=0; i<number_of_exceptions; i++) {
            byte[] exception_index_bytes = byteDashboard.nextN(2);
            int exception_index = ByteUtils.bytesToInt(exception_index_bytes, 0);
            this.exception_index_list.add(exception_index);

            String value = constantPool.getConstantString(exception_index, CPConst.CONSTANT_Class);
            list.add(value);
        }
        this.value = StringUtils.list2str(list, "[", "]", ", ");
    }

    public int getNumberOfExceptions() {
        return number_of_exceptions;
    }

    public List<Integer> getExceptionIndexList() {
        return exception_index_list;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitExceptions(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();
        list.add("Value='" + this.getValue() + "'");
        list.add("ExceptionIndex='" + StringUtils.list2str(this.exception_index_list, "[", "]", ",") + "'");
        list.add("HexCode='" + super.getHexCode() + "'");

        String content = StringUtils.list2str(list, ", ");

        StringBuilder buf = new StringBuilder();
        buf.append(this.getName() + " {");
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}
