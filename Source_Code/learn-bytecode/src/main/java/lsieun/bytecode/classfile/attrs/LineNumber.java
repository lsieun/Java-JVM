package lsieun.bytecode.classfile.attrs;

import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.ByteUtils;

public class LineNumber {
    private final int start_pc;
    private final int line_number;

    public LineNumber(ByteDashboard byteDashboard) {
        byte[] start_pc_bytes = byteDashboard.nextN(2);
        byte[] line_number_bytes = byteDashboard.nextN(2);
        this.start_pc = ByteUtils.bytesToInt(start_pc_bytes, 0);
        this.line_number = ByteUtils.bytesToInt(line_number_bytes, 0);
    }

    public int getStartPC() {
        return start_pc;
    }

    public int getLineNumber() {
        return line_number;
    }
}
