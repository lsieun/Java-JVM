package lsieun.bytecode.classfile.attrs;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.basic.CPConst;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class ExceptionTable {
    private final int start_pc;
    private final int end_pc;
    private final int handler_pc;
    private final int catch_type;
    private final String exceptionType;

    public ExceptionTable(ByteDashboard byteDashboard, ConstantPool constantPool) {
        byte[] start_pc_bytes = byteDashboard.nextN(2);
        byte[] end_pc_bytes = byteDashboard.nextN(2);
        byte[] handler_pc_bytes = byteDashboard.nextN(2);
        byte[] catch_type_bytes = byteDashboard.nextN(2);

        this.start_pc = ByteUtils.bytesToInt(start_pc_bytes, 0);
        this.end_pc = ByteUtils.bytesToInt(end_pc_bytes, 0);
        this.handler_pc = ByteUtils.bytesToInt(handler_pc_bytes, 0);
        this.catch_type = ByteUtils.bytesToInt(catch_type_bytes, 0);

        String exceptionType = "";
        if(catch_type == 0) {
            // If the value of the 'catch_type' item is zero, this exception handler is called
            // for all exceptions.
            exceptionType = "All";
        }
        else {
            exceptionType = constantPool.getConstant(catch_type, CPConst.CONSTANT_Class).getValue();
        }

        exceptionType = exceptionType.replaceAll("/", ".");
        this.exceptionType = exceptionType;
    }

    public int getStartPC() {
        return start_pc;
    }

    public int getEndPC() {
        return end_pc;
    }

    public int getHandlerPC() {
        return handler_pc;
    }

    public int getCatchType() {
        return catch_type;
    }

    public String getExceptionType() {
        return exceptionType;
    }
}
