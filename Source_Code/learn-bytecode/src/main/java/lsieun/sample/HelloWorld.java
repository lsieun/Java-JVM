package lsieun.sample;

public class HelloWorld {
    private int intValue;

    public int getValue() {
        return intValue;
    }

    public void setValue(int intValue) {
        this.intValue = intValue;
    }

    public void checkAndSetValue(int intValue) {
        if (intValue >= 0) {
            this.intValue = intValue;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
