package split;


public class ReusableBufferSplitter implements StringSplitter {

    public void split(String value, char separator, SplitValue splitValue) {
        StringBuilder sb = new StringBuilder();
        int length = value.length();

        for (int index = 0; index < length; index++) {

            char ch = value.charAt(index);

            if (ch == separator) {
                splitValue.nextToken(sb);
                sb.delete(0, sb.length());
            } else {
                sb.append(ch);
            }
        }

        if (sb.length() > 0) {
            splitValue.nextToken(sb);
        }
    }
}
