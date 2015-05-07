package queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class QueueTest {


    public static void main(String[] args) throws InterruptedException {


        long noOfItems = 100000 * 10 * 10;
        int queueSize = 100000;
        Queue<Long> task = null;
        int noofIte = 5;
        int noOfConsumer = 5;

        System.out.println("Start Warm Up");

        for (int x = 0; x < noofIte; x++) {
            task = new LockFreeBoundedQueue<Long>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }


        for (int x = 0; x < noofIte; x++) {
            task = new ArrayBlockingQueue<Long>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }

        System.out.println("End Warm Up");

        System.out.println("Start Test Now");
        noofIte = 2;

        for (int x = 0; x < noofIte; x++) {
            task = new LockFreeBoundedQueue<Long>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }

        for (int x = 0; x < noofIte; x++) {
            task = new ConcurrentLinkedQueue<Long>();
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }


        for (int x = 0; x < noofIte; x++) {
            task = new ArrayBlockingQueue<Long>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }

    }

    private static void buildAndExecute(long numberOfElement, int noofCon, int boundSize, Queue<Long> task) throws InterruptedException {

        AtomicLong producerCount = new AtomicLong();
        Runnable[] consumerThread = new Runnable[noofCon];
        for (int cnt = 0; cnt < noofCon; cnt++) {
            consumerThread[cnt] = new Consumer(task);
        }

        int noofPro = 1;
        Runnable[] producerThread = new Runnable[noofPro];
        for (int cnt = 0; cnt < noofPro; cnt++) {
            producerThread[cnt] = new Producer(numberOfElement,
                    task, producerCount);
        }
        long start = System.currentTimeMillis();
        execute(numberOfElement, task, producerThread, consumerThread);
        System.out.println(" Total Time for " + task.getClass() + " \t " + (System.currentTimeMillis() - start));
        System.out.println("*****************");
    }

    private static void execute(long numberOfElement, Queue<Long> task,
                                Runnable[] producer, Runnable[] consumer)
            throws InterruptedException {

        Thread[] pThread = new Thread[producer.length];
        for (int x = 0; x < producer.length; x++) {
            pThread[x] = new Thread(producer[x]);
        }

        Thread[] conThread = new Thread[consumer.length];
        for (int x = 0; x < consumer.length; x++) {
            conThread[x] = new Thread(consumer[x]);
        }

        for (int x = 0; x < producer.length; x++) {
            pThread[x].start();
        }

        for (int x = 0; x < conThread.length; x++) {
            conThread[x].start();
        }

        for (int x = 0; x < producer.length; x++) {
            pThread[x].join();
        }

        for (int x = 0; x < consumer.length; x++) {
            conThread[x].join();
        }


    }

}

class Producer implements Runnable {
    private long numbersToProduce;
    private Queue<Long> task;
    private AtomicLong itemsCount;

    public Producer(long numbersToProduce, Queue<Long> task,
                    AtomicLong itemsCount) {
        this.numbersToProduce = numbersToProduce;
        this.task = task;
        this.itemsCount = itemsCount;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        long lastNumber = -1;

        for (; ; ) {
            long curr = itemsCount.get();
            if (curr >= numbersToProduce)
                break;
            if (task.offer(curr + 1)) {
                lastNumber = curr + 1;
                itemsCount.incrementAndGet();
            }
        }

        System.out.println(this.getClass() + " via " + task.getClass()
                + " time (ms) " + (System.currentTimeMillis() - start)
                + " Last Number " + lastNumber);
        while (!task.offer(-1l)) ;
    }

}

class Consumer implements Runnable {

    private Queue<Long> task;

    public Consumer(Queue<Long> task) {
        this.task = task;

    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        long lastValue = -1;

        for (; ; ) {
            {
                Long value = task.poll();
                if (value != null) {
                    if (value == -1) {
                        while (!task.offer(-1l)) ;
                        break;
                    }
                    ;
                    lastValue = value;
                }


            }

        }

        System.out.println(this.getClass() + " Via " + task.getClass()
                + " time (ms) " + (System.currentTimeMillis() - start)
                + " Last Value " + lastValue);
    }

}

class LockFreeBoundedQueue<E> implements Queue<E> {

    private Object[] values;
    private AtomicLong tailPointer = new AtomicLong();
    private AtomicLong headPointer = new AtomicLong();

    public LockFreeBoundedQueue(int size) {
        values = new Object[size];
    }

    @Override
    public boolean offer(E e) {
        long curTail = tailPointer.get();
        long diff = curTail - values.length;
        if (headPointer.get() <= diff) {
            return false;
        }
        values[(int) (curTail % values.length)] = e;
        while (tailPointer.compareAndSet(curTail, curTail + 1)) ;
        return true;
    }

    @Override
    public E poll() {
        long curHead = headPointer.get();
        if (curHead >= tailPointer.get())
            return null;
        if (!headPointer.compareAndSet(curHead, curHead + 1)) {
            return null;
        }
        int index = (int) curHead % values.length;
        @SuppressWarnings("unchecked")
        E value = (E) values[index];
        values[index] = null;
        return value;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean add(E e) {

        return false;
    }

    @Override
    public E remove() {

        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

}