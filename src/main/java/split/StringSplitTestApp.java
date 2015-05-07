package split;

/*
-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps
 */
public class StringSplitTestApp {

    public static void main(String... args) throws InterruptedException {

        StringSplitter[] splitter = {new JDKStringSplitter(), new StringTokenizerSplitter(), new ReusableBufferSplitter()};
        String[] values = {
                "this,is,simple,test",
                "lets,see,how,it,works",
                "this,is,very,simple,test"
        };

        int MILLION = 100_00_00 * 10;
        System.out.println("*******Warmup******");
        execute(splitter, values, MILLION);
        System.gc();
        Thread.sleep(1000 * 5L);

        System.out.println("*******Real******");
        for (int times = 0; times < 10; times++) {
            execute(splitter, values, MILLION);
        }

    }

    private static void execute(StringSplitter[] splitter, String[] values, int MILLION) {
        for (StringSplitter s : splitter) {
            long count = 0;
            long start = System.currentTimeMillis();
            for (int times = 0; times < MILLION; times++) {
                for (String value : values) {
                    SplitValueCounter splitValue = new SplitValueCounter();
                    s.split(value, ',', splitValue);
                    count += splitValue.count;
                }
            }
            long total = System.currentTimeMillis() - start;
            System.out.println(String.format("Class %s produced %s tokens in %s ms", s.getClass().getSimpleName(), count, total));
        }
    }

    static class SplitValueCounter implements SplitValue {
        int count;

        @Override
        public void nextToken(CharSequence value) {
            count++;
        }
    }
}
