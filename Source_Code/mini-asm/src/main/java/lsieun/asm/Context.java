package lsieun.asm;

final class Context {
    /** The buffer used to read strings in the constant pool. */
    char[] charBuffer;

    // Information about the current method, i.e. the one read in the current (or latest) call
    // to {@link ClassReader#readMethod()}.

    /** The access flags of the current method. */
    int currentMethodAccessFlags;

    /** The name of the current method. */
    String currentMethodName;

    /** The descriptor of the current method. */
    String currentMethodDescriptor;
}
