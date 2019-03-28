package lsieun.bytecode.utils;

public class ByteDashboard {
    private String url;
    private byte[] bytes;

    private int start;
    private int stop;
    private int index;

    public ByteDashboard(String url, byte[] bytes) {
        this.url = url;
        this.bytes = bytes;
        this.start = 0;
        this.stop = bytes.length;
        this.index = this.start;
    }

    // region getter & setter
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
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

    public void reset() {
        this.index = this.start;
    }

    @Override
    public String toString() {
        return "ByteDashboard {" +
                "start=" + start +
                ", stop=" + stop +
                ", index=" + index +
                ", url='" + url + '\'' +
                '}';
    }
}
