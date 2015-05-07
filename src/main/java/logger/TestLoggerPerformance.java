package logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class TestLoggerPerformance {

    public static void main(String... a) throws Exception {
        LoggerFacade javaLogger = LoggerFacade.createInstance(LoggerFacade.LoggerType.JavaLogger, "javalogger.log");
        LoggerFacade fastLogger = LoggerFacade.createInstance(LoggerFacade.LoggerType.FastLogger, "fastlogger.log");

        Random r = new Random();
        int[] numbers = new int[100000 * 10];
        for (int x = 0; x < numbers.length; x++) {
            numbers[x] = r.nextInt(1000);
        }
        ExecutorService es = Executors.newFixedThreadPool(1);


        System.out.println("Warm Up started");
        submitForSum(null, numbers, es);
        submitForSum(javaLogger, numbers, es);
        submitForSum(fastLogger, numbers, es);
        System.out.println("Warm Up done");
        System.out.println("**************************Start Test Now*******");
        System.out.println("real test started");
        for (int x = 0; x < 20; x++) {
            System.out.println("RESULT for " + x);
            submitForSum(null, numbers, es);
            submitForSum(javaLogger, numbers, es);
            submitForSum(fastLogger, numbers, es);
        }
        es.shutdownNow();
        fastLogger.shutdownNow();
        javaLogger.shutdownNow();
    }

    private static void submitForSum(LoggerFacade javaLogger, int[] numbers, ExecutorService es) throws InterruptedException, ExecutionException {
        List<Future<Long>> ftList = new ArrayList<Future<Long>>();
        int rangeSize = 10000;
        long start = System.currentTimeMillis();
        for (int x = 0; x < numbers.length; ) {
            int endIndex = Math.min(x + rangeSize, numbers.length);
            ftList.add(es.submit(new SumTask(javaLogger, x, endIndex, numbers)));
            x = endIndex;
        }
        long sum = 0;
        for (Future<Long> f : ftList) {
            sum += f.get();
        }
        long end = System.currentTimeMillis();
        System.out.println("Type : " + (javaLogger == null ? "No Logger " : javaLogger.getClass()) + " Time :" + (end - start) + " Sum :" + sum);
    }


    private static class SumTask implements Callable<Long> {
        private int start;
        private int end;
        private int[] numbers;
        private LoggerFacade log;

        public SumTask(LoggerFacade log, int start, int end, int[] numbers) {
            super();
            this.start = start;
            this.end = end;
            this.numbers = numbers;
            this.log = log;
        }

        @Override
        public Long call() throws Exception {
            long sum = 0;
            if (log != null)
                log.info(Thread.currentThread().getName() + " Started calc for range " + start + " to " + end);

            for (int cnt = start; cnt < end; cnt++) {
                sum += numbers[cnt];
            }

            if (log != null)
                log.info(Thread.currentThread().getName() + " finished calc for range " + start + " to " + end);
            return sum;
        }
    }
}
