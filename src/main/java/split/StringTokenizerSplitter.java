package split;


import java.util.StringTokenizer;

public class StringTokenizerSplitter implements StringSplitter {

    @Override
    public void split(String value, char separator, SplitValue splitValue) {

        StringTokenizer st = new StringTokenizer(value, String.valueOf(separator));
        for (; st.hasMoreTokens(); ) {
            splitValue.nextToken(st.nextToken());
        }
    }
}
