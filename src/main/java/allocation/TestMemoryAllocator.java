package allocation;


import java.util.concurrent.TimeUnit;

public class TestMemoryAllocator {

    public static void main(String... args) {

        if (args == null || args.length == 0) {
            args = new String[]{Allocator.Type.OFFHEAP.name(), "5000000"};
        }

        final int nElements = Integer.valueOf(args[1]);
        System.out.println("testing objects[" + nElements + "]");

        final boolean randomAccess = true;

        final int[] idx;
        if (randomAccess) {
            idx = new int[nElements];
            for (int i = 0; i < nElements; i++) {
                idx[i] = (int) (Math.random() * nElements);
            }
        } else {
            idx = null;
        }

        final ObjectType[] types = new ObjectType[]{Allocator.allocate(Allocator.Type.valueOf(args[0]), nElements)};
        int ONE_MILLION = 1000000;

        final int len = types.length;

        for (int x = 0; x < 100; x++) {
            for (int n = 0; n < len; n++) {
                final ObjectType t = types[n];

                long writeStart = System.nanoTime();
                int resWrite = write(t, nElements);
                long totalWrite = System.nanoTime() - writeStart;

                long readStart = System.nanoTime();
                int resRead = read(t, nElements);
                long totalRead = System.nanoTime() - readStart;

                long rreadStart = System.nanoTime();
                int rresRead = randomRead(t, nElements, idx);
                long rtotalRead = System.nanoTime() - readStart;

                double writeMs = totalWrite / 1000000d;
                double readMs = totalRead / 1000000d;
                double rreadMs = rtotalRead / 1000000d;

                System.out.println(String.format("[%2s] %s - [Write %16s ms, Read %16s ms, Rand Read %16s ms], "
                                + "Op/Sec(Millions)[Write %6s, Read %6s, Rand Read %6s]: %16s %16s %16s",
                        x, t.getClass().getName(), writeMs, readMs, rreadMs,
                        (TimeUnit.SECONDS.toNanos(nElements) / totalWrite) / ONE_MILLION,
                        (TimeUnit.SECONDS.toNanos(nElements) / totalRead) / ONE_MILLION,
                        (TimeUnit.SECONDS.toNanos(nElements) / rtotalRead) / ONE_MILLION,
                        resWrite, resRead, rresRead));
            }
        }
    }

    public static int write(ObjectType t, int items) {
        int index = 0;
        for (; index < items; index++) {
            t.navigate(index);
            t.setByte((byte) index);
            t.setInt(index);
            t.setLong(index);
        }
        return index;
    }

    public static int read(ObjectType t, int items) {
        int sum = 0;

        for (int index = 0; index < items; index++) {
            t.navigate(index);

            /* consume ie use all read values to avoid dead code elimination */
            sum += t.getByte() + t.getInt() + (int) t.getLong();
        }
        return sum;
    }

    public static int randomRead(ObjectType t, int items, final int[] idx) {
        int sum = 0;

        for (int index = 0; index < items; index++) {
            t.navigate(idx[index]);

            /* consume ie use all read values to avoid dead code elimination */
            sum += t.getByte() + t.getInt() + (int) t.getLong();
        }
        return sum;
    }
}

