package lsieun.bytecode.classfile.attrs.code;

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

    /**
     * @param start_pc Program Counter (PC) corresponds to
     * @param line_number line number in source file
     */
    public LineNumber(final int start_pc, final int line_number) {
        this.start_pc = (short) start_pc;
        this.line_number = (short)line_number;
    }

    public int getStartPC() {
        return start_pc;
    }

    public int getLineNumber() {
        return line_number;
    }
}
