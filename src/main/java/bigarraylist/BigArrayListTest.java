package bigarraylist;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BigArrayListTest {


    public static void main(String[] args) throws IOException {

        String testType = args[0];
        int elementToAdd = Integer.parseInt(args[1]);

        ListBuilder<Instrument> builder = null;

        if ("AL".equals(testType)) {
            builder = new ArrayListBuilder();
        } else if ("BAL".equals(testType)) {
            builder = new BigArrayListBuilder(elementToAdd / 20);
        }

        int warmUp = 50;
        execute(elementToAdd, builder, warmUp);
        System.out.println("Warm Up Done");
        System.gc();
        int itr = 10;
        execute(elementToAdd, builder, itr);

    }

    private static void execute(int size, ListBuilder<Instrument> builder, int itr) {
        for (int cnt = 0; cnt < itr; cnt++) {

            long start = System.nanoTime();
            List<Instrument> v = builder.build(size);
            long write = System.nanoTime() - start;

            start = System.nanoTime();
            builder.process(v);
            long read = System.nanoTime() - start;
            System.out.println(String.format("[%s] %s Write took %s ms, Write/tp :%s , Read Took %s ms, Read/tp %s",
                    cnt, builder.getClass().getSimpleName(),
                    TimeUnit.NANOSECONDS.toMillis(write), TimeUnit.SECONDS.toNanos(size) / write,
                    TimeUnit.NANOSECONDS.toMillis(read), TimeUnit.SECONDS.toNanos(size) / read));
        }
    }

    private static interface ListBuilder<T> {
        public List<T> build(int size);

        public void process(List<T> values);
    }

    private static class ArrayListBuilder implements ListBuilder<Instrument> {
        @Override
        public List<Instrument> build(int size) {
            List<Instrument> values = new ArrayList<>();
            for (int x = 0; x < size; x++) {
                values.add(new Instrument(x, x * 100d));
            }
            return values;
        }

        @Override
        public void process(List<Instrument> values) {
            long value = 0;
            for (int x = 0; x < values.size(); x++) {
                value += values.get(x).getId();
            }
        }

    }

    private static class BigArrayListBuilder implements ListBuilder<Instrument> {
        int noOfElement;

        public BigArrayListBuilder(int noOfElement) {
            this.noOfElement = noOfElement;
        }

        @Override
        public List<Instrument> build(int size) {
            BigArrayList<Instrument> values = new BigArrayList<>(
                    new BigArrayReaderWriter<Instrument>() {
                        @Override
                        public void write(Instrument value, ByteBuffer buffer) {
                            buffer.putInt(value.getId());
                            buffer.putDouble(value.getPrice());

                        }

                        @Override
                        public Instrument read(ByteBuffer buffer) {
                            return new Instrument(buffer.getInt(),
                                    buffer.getDouble());
                        }

                        @Override
                        public int messageSize() {
                            return 12;
                        }

                    }, noOfElement);
            for (int x = 0; x < size; x++) {
                values.add(new Instrument(x, x * 100d));
            }
            return values;
        }

        @Override
        public void process(List<Instrument> values) {
            long value = 0;
            for (int x = 0; x < values.size(); x++) {
                value += values.get(x).getId();
            }
        }

    }

    private static class Instrument {
        private int id;
        private double price;

        public Instrument(int id, double price) {
            super();
            this.id = id;
            this.price = price;

        }

        public int getId() {
            return id;
        }

        public double getPrice() {
            return price;
        }

    }

}
