package org.openjdk.jmh.samples.generated;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.annotation.Generated;

import org.openjdk.jmh.logic.Loop;
import org.openjdk.jmh.logic.Global;
import org.openjdk.jmh.logic.BlackHole;
import org.openjdk.jmh.logic.results.Result;
import org.openjdk.jmh.logic.results.OpsPerTimeUnit;
import org.openjdk.jmh.logic.results.AverageTimePerOp;
import org.openjdk.jmh.logic.results.SampleTimePerOp;
import org.openjdk.jmh.logic.results.SingleShotTime;
import org.openjdk.jmh.util.internal.SampleBuffer;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.logic.results.RawResultPair;

@Generated("org.openjdk.jmh.processor.internal.GenerateMicroBenchmarkProcessor")
public final class JMHSample_01_HelloWorld {

    public volatile int pad01, pad02, pad03, pad04, pad05, pad06, pad07, pad08;
    public volatile int pad11, pad12, pad13, pad24, pad15, pad16, pad17, pad18;
    public volatile int pad21, pad22, pad23, pad34, pad25, pad26, pad27, pad28;
    public volatile int pad31, pad32, pad33, pad44, pad35, pad36, pad37, pad38;
    

    @Threads(1)
    public Result wellHelloThere_Throughput(Loop loop) throws Throwable { 

        if (!threadId_inited) {
            threadId = threadSelector.getAndIncrement();
            threadId_inited = true;
        }
        int groupThreadCount = 1;
        int groupId = threadId / groupThreadCount;
        int siblingId = threadId % groupThreadCount;

        Global global = loop.global;

        if (0 <= siblingId && siblingId < 1) { 
            JMHSample_01_HelloWorld_jmh l_bench = tryInit_f_bench(new JMHSample_01_HelloWorld_jmh());
            BlackHole_jmh l_blackhole = tryInit_f_blackhole(new BlackHole_jmh());

            loop.preSetup();

            global.announceWarmupReady();
            while (global.warmupShouldWait) {
                l_bench.wellHelloThere();
            }

            loop.enable();
            RawResultPair res = wellHelloThere_Throughput_measurementLoop(loop, l_bench, l_blackhole);
            global.announceWarmdownReady();
            while (global.warmdownShouldWait) {
                l_bench.wellHelloThere();
            }
            loop.preTearDown();

            if (loop.isLastIteration()) {
            }
            return new OpsPerTimeUnit("wellHelloThere", res.operations, res.time, (loop.timeUnit != null) ? loop.timeUnit : TimeUnit.MILLISECONDS);
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");

    }
    public  RawResultPair wellHelloThere_Throughput_measurementLoop(Loop loop, JMHSample_01_HelloWorld_jmh l_bench, BlackHole_jmh l_blackhole) throws Throwable {
        long operations = 0;
        long realTime = 0;
        long startTime = System.nanoTime();
        do {
            l_bench.wellHelloThere();
            operations++;
        } while(!loop.isDone);
        long stopTime = System.nanoTime();
        return new RawResultPair(operations * 1L,  (realTime > 0) ? realTime : (stopTime - startTime));
    }


    @Threads(1)
    public Result wellHelloThere_AverageTime(Loop loop) throws Throwable { 
        if (!threadId_inited) {
            threadId = threadSelector.getAndIncrement();
            threadId_inited = true;
        }
        int groupThreadCount = 1;
        int groupId = threadId / groupThreadCount;
        int siblingId = threadId % groupThreadCount;

        Global global = loop.global;

        if (0 <= siblingId && siblingId < 1) { 
            JMHSample_01_HelloWorld_jmh l_bench = tryInit_f_bench(new JMHSample_01_HelloWorld_jmh());
            BlackHole_jmh l_blackhole = tryInit_f_blackhole(new BlackHole_jmh());

            loop.preSetup();

            global.announceWarmupReady();
            while (global.warmupShouldWait) {
                l_bench.wellHelloThere();
            }

            loop.enable();
            RawResultPair res = wellHelloThere_AverageTime_measurementLoop(loop, l_bench, l_blackhole);
            global.announceWarmdownReady();
            while (global.warmdownShouldWait) {
                l_bench.wellHelloThere();
            }
            loop.preTearDown();

            if (loop.isLastIteration()) {
            }
            return new AverageTimePerOp("wellHelloThere", res.operations, res.time, (loop.timeUnit != null) ? loop.timeUnit : TimeUnit.MILLISECONDS);
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");

    }
    public  RawResultPair wellHelloThere_AverageTime_measurementLoop(Loop loop, JMHSample_01_HelloWorld_jmh l_bench, BlackHole_jmh l_blackhole) throws Throwable {
        long operations = 0;
        long realTime = 0;
        long start = System.nanoTime();
        do {
            l_bench.wellHelloThere();
            operations++;
        } while(!loop.isDone);
        long end = System.nanoTime();
        return new RawResultPair(operations * 1L,  (realTime > 0) ? realTime : (end - start));
    }


    @Threads(1)
    public Result wellHelloThere_SampleTime(Loop loop) throws Throwable { 

        if (!threadId_inited) {
            threadId = threadSelector.getAndIncrement();
            threadId_inited = true;
        }
        int groupThreadCount = 1;
        int groupId = threadId / groupThreadCount;
        int siblingId = threadId % groupThreadCount;

        Global global = loop.global;

        if (0 <= siblingId && siblingId < 1) { 
            JMHSample_01_HelloWorld_jmh l_bench = tryInit_f_bench(new JMHSample_01_HelloWorld_jmh());
            BlackHole_jmh l_blackhole = tryInit_f_blackhole(new BlackHole_jmh());

            loop.preSetup();

            global.announceWarmupReady();
            while (global.warmupShouldWait) {
                l_bench.wellHelloThere();
            }

            Result res = wellHelloThere_SampleTime_measurementLoop(loop, l_bench, l_blackhole);
            global.announceWarmdownReady();
            while (global.warmdownShouldWait) {
                l_bench.wellHelloThere();
            }

            loop.preTearDown();

            if (loop.isLastIteration()) {
            }
            return res;
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");
    }
    public  Result wellHelloThere_SampleTime_measurementLoop(Loop loop, JMHSample_01_HelloWorld_jmh l_bench, BlackHole_jmh l_blackhole) throws Throwable {
        SampleBuffer buffer = new SampleBuffer();
        long realTime = 0;
        long rnd = System.nanoTime();
        long rndMask = 1;
        long time = 0;
        loop.enable();
        do {
            rnd = (rnd * 0x5DEECE66DL + 0xBL) & (0xFFFFFFFFFFFFL);
            boolean sample = (rnd & rndMask) == 0;
            if (sample) {
                time = System.nanoTime();
            }
            l_bench.wellHelloThere();
            if (sample) {
                boolean flipped = buffer.add(System.nanoTime() - time);
                if (flipped) {
                    if (rndMask != 0xFFFFFFFFFFFFL) {
                        rndMask = (rndMask << 1) + 1;
                    }
                }
            }
        } while(!loop.isDone);
        return new SampleTimePerOp("wellHelloThere", buffer, (loop.timeUnit != null) ? loop.timeUnit : TimeUnit.MILLISECONDS);
    }


    @Threads(1)
    public Result wellHelloThere_SingleShotTime(Loop loop) throws Throwable { 
        if (!threadId_inited) {
            threadId = threadSelector.getAndIncrement();
            threadId_inited = true;
        }
        int groupThreadCount = 1;
        int groupId = threadId / groupThreadCount;
        int siblingId = threadId % groupThreadCount;

        Global global = loop.global;

        long realTime = 0;
        if (0 <= siblingId && siblingId < 1) { 
            JMHSample_01_HelloWorld_jmh l_bench = tryInit_f_bench(new JMHSample_01_HelloWorld_jmh());
            BlackHole_jmh l_blackhole = tryInit_f_blackhole(new BlackHole_jmh());

            loop.preSetup();

            long time1 = System.nanoTime();
            l_bench.wellHelloThere();
            long time2 = System.nanoTime();
            loop.preTearDown();

            if (loop.isLastIteration()) {
            }
            return new SingleShotTime("wellHelloThere", (realTime > 0) ? realTime : (time2 - time1), (loop.timeUnit != null) ? loop.timeUnit : TimeUnit.MILLISECONDS);
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");

    }
    
    JMHSample_01_HelloWorld_jmh f_bench;
    
    JMHSample_01_HelloWorld_jmh tryInit_f_bench(JMHSample_01_HelloWorld_jmh val) throws Throwable {
        if (f_bench == null) {
                    val.readyTrial = true;
              f_bench = val;
        }
        return f_bench;
    }
    
    BlackHole_jmh f_blackhole;
    
    BlackHole_jmh tryInit_f_blackhole(BlackHole_jmh val) throws Throwable {
        if (f_blackhole == null) {
                    val.readyTrial = true;
              f_blackhole = val;
        }
        return f_blackhole;
    }

    private static final java.util.concurrent.atomic.AtomicInteger threadSelector = new java.util.concurrent.atomic.AtomicInteger();
    private int threadId = 0;
    private boolean threadId_inited = false;

    static final class JMHSample_01_HelloWorld_jmh extends org.openjdk.jmh.samples.JMHSample_01_HelloWorld {
        private volatile int jmh_auto_generated_pad01;
        private volatile int jmh_auto_generated_pad02;
        private volatile int jmh_auto_generated_pad03;
        private volatile int jmh_auto_generated_pad04;
        private volatile int jmh_auto_generated_pad05;
        private volatile int jmh_auto_generated_pad06;
        private volatile int jmh_auto_generated_pad07;
        private volatile int jmh_auto_generated_pad08;
        private volatile int jmh_auto_generated_pad09;
        private volatile int jmh_auto_generated_pad10;
        private volatile int jmh_auto_generated_pad11;
        private volatile int jmh_auto_generated_pad12;
        private volatile int jmh_auto_generated_pad13;
        private volatile int jmh_auto_generated_pad14;
        private volatile int jmh_auto_generated_pad15;
        private volatile int jmh_auto_generated_pad16;
        private volatile int jmh_auto_generated_pad17;
        private volatile int jmh_auto_generated_pad18;
        private volatile int jmh_auto_generated_pad19;
        private volatile int jmh_auto_generated_pad20;
        private volatile int jmh_auto_generated_pad21;
        private volatile int jmh_auto_generated_pad22;
        private volatile int jmh_auto_generated_pad23;
        private volatile int jmh_auto_generated_pad24;
        private volatile int jmh_auto_generated_pad25;
        private volatile int jmh_auto_generated_pad26;
        private volatile int jmh_auto_generated_pad27;
        private volatile int jmh_auto_generated_pad28;
        private volatile int jmh_auto_generated_pad29;
        private volatile int jmh_auto_generated_pad30;
        private volatile int jmh_auto_generated_pad31;
        private volatile int jmh_auto_generated_pad32;
    
        private boolean readyTrial;
        private boolean readyIteration;
        private boolean readyInvocation;
    }
    static final class BlackHole_jmh extends org.openjdk.jmh.logic.BlackHole {
        private volatile int jmh_auto_generated_pad01;
        private volatile int jmh_auto_generated_pad02;
        private volatile int jmh_auto_generated_pad03;
        private volatile int jmh_auto_generated_pad04;
        private volatile int jmh_auto_generated_pad05;
        private volatile int jmh_auto_generated_pad06;
        private volatile int jmh_auto_generated_pad07;
        private volatile int jmh_auto_generated_pad08;
        private volatile int jmh_auto_generated_pad09;
        private volatile int jmh_auto_generated_pad10;
        private volatile int jmh_auto_generated_pad11;
        private volatile int jmh_auto_generated_pad12;
        private volatile int jmh_auto_generated_pad13;
        private volatile int jmh_auto_generated_pad14;
        private volatile int jmh_auto_generated_pad15;
        private volatile int jmh_auto_generated_pad16;
        private volatile int jmh_auto_generated_pad17;
        private volatile int jmh_auto_generated_pad18;
        private volatile int jmh_auto_generated_pad19;
        private volatile int jmh_auto_generated_pad20;
        private volatile int jmh_auto_generated_pad21;
        private volatile int jmh_auto_generated_pad22;
        private volatile int jmh_auto_generated_pad23;
        private volatile int jmh_auto_generated_pad24;
        private volatile int jmh_auto_generated_pad25;
        private volatile int jmh_auto_generated_pad26;
        private volatile int jmh_auto_generated_pad27;
        private volatile int jmh_auto_generated_pad28;
        private volatile int jmh_auto_generated_pad29;
        private volatile int jmh_auto_generated_pad30;
        private volatile int jmh_auto_generated_pad31;
        private volatile int jmh_auto_generated_pad32;
    
        private boolean readyTrial;
        private boolean readyIteration;
        private boolean readyInvocation;
    }

}

