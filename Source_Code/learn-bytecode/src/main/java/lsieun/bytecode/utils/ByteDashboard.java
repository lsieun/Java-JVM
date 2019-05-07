package lsieun.bytecode.utils;

import lsieun.utils.radix.ByteUtils;

/**
 * ByteDashboard的本质就是将一个<code>byte[]</code>放到一个类里面，那么，这么做的目的是什么呢？就是为了“好玩”！<br/><br/>
 * <p>
 *     开个玩笑的啦！我们可以将byte[]想像成一本珍贵的“历史图书”，它里面记载了很多有用的信息，同时
 *     可以将ByteDashboard想像成一个“图书錧”，我们把这本“历史图书”放到了这个“图书錧”里。
 * </p><br/>
 * <p>
 *     （1）source字段，表示这本“历史图书”的来源，是个辅助的、不重要的信息。
 * </p>
 * <p>
 *     （2）start/stop/index三个字段。是为了记录这本“历史图书”的开始页码、结束页码和当前的阅读位置，这是最重要的信息了。
 * </p>
 * <p>
 *     （3）ByteDashboard类里的方法，都是围绕这4个字段来展开的。
 * </p>
 */
public class ByteDashboard {
    private String source;
    private final byte[] bytes;

    private int start;
    private int stop;
    private int index;

    public ByteDashboard(String source, byte[] bytes) {
        this.source = source;
        this.bytes = bytes;
        this.start = 0;
        this.stop = bytes.length;
        this.index = this.start;
    }

    // region getter & setter
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    // endregion

    public boolean hasNex() {
        if(index >= start && index < stop) return true;
        return false;
    }

    public byte next() {
        byte b = this.bytes[index];
        this.index++;
        return b;
    }

    public byte[] nextN(int n) {
        byte[] bytes = new byte[n];
        for(int i=0; i<n; i++) {
            byte b = this.bytes[index];
            this.index++;
            bytes[i] = b;
        }
        return bytes;
    }

    public byte peek() {
        byte b = this.bytes[index];
        return b;
    }

    public byte[] peekN(int n) {
        byte[] bytes = new byte[n];
        for(int i=0; i<n; i++) {
            int idx = this.index + i;
            byte b = this.bytes[idx];
            bytes[i] = b;
        }
        return bytes;
    }

    public byte[] peekN(int offset, int n) {
        byte[] bytes = new byte[n];
        for(int i=0; i<n; i++) {
            int idx = this.index + i + offset;
            byte b = this.bytes[idx];
            bytes[i] = b;
        }
        return bytes;
    }

    // region nextXXX
    /**
     * 这里不保留符号（正负的符号）
     * @return
     */
    public int nextByte() {
        byte[] bytes = nextN(1);
        return ByteUtils.bytesToInt(bytes, 0);
    }

    public int nextShort() {
        byte[] bytes = nextN(2);
        return ByteUtils.bytesToInt(bytes, 0);
    }

    public int nextInt() {
        byte[] bytes = nextN(4);
        return ByteUtils.bytesToInt(bytes, 0);
    }
    // endregion

    // region peekXXX
    public int peekByte() {
        byte[] bytes = peekN(1);
        return ByteUtils.bytesToInt(bytes, 0);
    }

    public int peekShort() {
        byte[] bytes = peekN(2);
        return ByteUtils.bytesToInt(bytes, 0);
    }

    public int peekInt() {
        byte[] bytes = peekN(4);
        return ByteUtils.bytesToInt(bytes, 0);
    }

    public int peekByte(int offset) {
        byte[] bytes = peekN(offset, 1);
        return ByteUtils.bytesToInt(bytes, 0);
    }

    public int peekShort(int offset) {
        byte[] bytes = peekN(offset, 2);
        return ByteUtils.bytesToInt(bytes, 0);
    }

    public int peekInt(int offset) {
        byte[] bytes = peekN(offset, 4);
        return ByteUtils.bytesToInt(bytes, 0);
    }
    // endregion

    // region readXXX
    /**
     * 这里保留符号（正负的符号）
     * @return
     */
    public byte readByte() {
        byte b = next();
        return b;
    }

    public short readShort() {
        byte[] bytes = nextN(2);
        return ByteUtils.toShort(bytes);
    }

    public int readInt() {
        byte[] bytes = nextN(4);
        return ByteUtils.toInt(bytes);
    }

    public long readLong() {
        byte[] bytes = nextN(8);
        return ByteUtils.toLong(bytes);
    }
    // endregion

    public void reset() {
        this.index = this.start;
    }

    @Override
    public String toString() {
        return "ByteDashboard {" +
                "start=" + start +
                ", stop=" + stop +
                ", index=" + index +
                ", source='" + source + '\'' +
                '}';
    }
}
