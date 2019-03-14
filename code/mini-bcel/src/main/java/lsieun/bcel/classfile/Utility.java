package lsieun.bcel.classfile;

public class Utility {

    /**
     * Shorten long class name <em>str</em>, i.e., chop off the <em>prefix</em>,
     * if the
     * class name starts with this string and the flag <em>chopit</em> is true.
     * Slashes <em>/</em> are converted to dots <em>.</em>.
     *
     * @param str The long class name
     * @param prefix The prefix the get rid off
     * @param chopit Flag that determines whether chopping is executed or not
     * @return Compacted class name
     */
    public static String compactClassName(String str, final String prefix, final boolean chopit) {
        final int len = prefix.length();
        str = str.replace('/', '.'); // Is `/' on all systems, even DOS
        if (chopit) {
            // If string starts with `prefix' and contains no further dots
            if (str.startsWith(prefix) && (str.substring(len).indexOf('.') == -1)) {
                str = str.substring(len);
            }
        }
        return str;
    }

    /**
     * Shorten long class names, <em>java/lang/String</em> becomes
     * <em>java.lang.String</em>,
     * e.g.. If <em>chopit</em> is <em>true</em> the prefix <em>java.lang</em>
     * is also removed.
     *
     * @param str The long class name
     * @param chopit Flag that determines whether chopping is executed or not
     * @return Compacted class name
     */
    public static String compactClassName(final String str, final boolean chopit) {
        return compactClassName(str, "java.lang.", chopit);
    }

    /**
     * Shorten long class names, <em>java/lang/String</em> becomes
     * <em>String</em>.
     *
     * @param str The long class name
     * @return Compacted class name
     */
    public static String compactClassName( final String str ) {
        return compactClassName(str, true);
    }

    /**
     * Escape all occurences of newline chars '\n', quotes \", etc.
     */
    public static String convertString( final String label ) {
        final char[] ch = label.toCharArray();
        final StringBuilder buf = new StringBuilder();
        for (final char element : ch) {
            switch (element) {
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\"':
                    buf.append("\\\"");
                    break;
                case '\'':
                    buf.append("\\'");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                default:
                    buf.append(element);
                    break;
            }
        }
        return buf.toString();
    }
}
