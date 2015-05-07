package list;

public class MemoryTest {

    @SuppressWarnings("unused")
    public static void main(String... args) throws Exception {

        Object handle = create(args[0]);

        long memory = usedMemory();
        handle = null;
        lotsOfGC();
        memory = usedMemory();
        handle = create(args[0]);
        lotsOfGC();
        long totalMeory = usedMemory() - memory;
        System.out.println(args[0] + " Took " + totalMeory / 1024 + " KB");

    }

    private static Object create(String type) throws Exception {
        return ObjectFactory.build(type, "http://www.umich.edu/~umfandsf/other/ebooks/alice30.txt");
    }

    public static long usedMemory() {
        return Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();
    }

    public static void lotsOfGC() {
        for (int i = 0; i < 20; i++) {
            System.gc();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
    }
}
