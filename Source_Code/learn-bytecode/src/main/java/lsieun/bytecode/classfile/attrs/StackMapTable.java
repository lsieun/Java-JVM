package lsieun.bytecode.classfile.attrs;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

// FIXME: 这里还没有处理
public final class StackMapTable extends AttributeInfo {
    private final int number_of_entries;
    private final List<StackMap> stack_map_list;

    public StackMapTable(ByteDashboard byteDashboard, ConstantPool constantPool) {
        super(byteDashboard, constantPool, true);

        byte[] number_of_entries_bytes = byteDashboard.nextN(2);
        this.number_of_entries = ByteUtils.bytesToInt(number_of_entries_bytes, 0);
        this.stack_map_list = new ArrayList();
    }
}
