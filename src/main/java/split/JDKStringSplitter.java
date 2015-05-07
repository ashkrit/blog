package split;


public class JDKStringSplitter implements StringSplitter {

    @Override
    public void split(String value, char separator, SplitValue splitValue) {

        String[] values = value.split(String.valueOf(separator));
        for (String token : values) {
            splitValue.nextToken(token);
        }
    }
}
