package logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public abstract class LoggerFacade {

    public enum LoggerType {
        JavaLogger,
        FastLogger
    }

    public static LoggerFacade createInstance(LoggerType type, String fileName) throws SecurityException, IOException {
        if (LoggerType.JavaLogger == type) {
            return new JavaLogger(fileName);
        } else if (LoggerType.FastLogger == type) {
            return new FastLogger(fileName);
        }
        return null;
    }

    public abstract void info(String message);

    public abstract void shutdownNow();

}

class JavaLogger extends LoggerFacade {
    private Logger logger;

    public JavaLogger(String fileName) throws SecurityException, IOException {
        logger = Logger.getLogger(JavaLogger.class.getName());
        FileHandler fh = new FileHandler("C:/tmp/" + fileName);
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        logger.setLevel(Level.INFO);
        logger.setUseParentHandlers(false);
    }

    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void shutdownNow() {
    }
}


class FastLogger extends LoggerFacade {
    private SmartLogger logger;

    public FastLogger(String fileName) throws SecurityException, IOException {
        logger = new SmartLogger(new File("C:/tmp/" + fileName), 1024);
    }

    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void shutdownNow() {
        logger.shutdownNow();
    }
}

class SmartLogger {

    private File logFile;
    private Queue<String> messgeQueue = new ConcurrentLinkedQueue<String>();
    private ByteBuffer buffer;
    private ExecutorService exe;
    private volatile boolean signalRequired;
    private volatile boolean endLogging;
    private final Lock lock = new ReentrantLock();
    private final Condition messageCondition = lock.newCondition();
    private FileChannel fc;
    private Future<Void> ft;

    public SmartLogger(File logFile, int bufferSize) throws IOException {
        super();
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.logFile = logFile;
        logFile.createNewFile();
        //this.fc = new RandomAccessFile(this.logFile, "rw").getChannel();
        this.fc = new FileOutputStream(this.logFile).getChannel();
        this.exe = Executors.newSingleThreadExecutor();
        this.ft = exe.submit(new LogMessageCallable());
    }

    private class LogMessageCallable implements Callable<Void> {
        @Override
        public Void call() throws Exception {

            ByteBuffer buffer = SmartLogger.this.buffer;
            while (!Thread.currentThread().isInterrupted()) {
                if (endLogging) break;
                while (messgeQueue.peek() == null) {
                    signalRequired = true;
                    if (endLogging) return null;
                    waitForMessage();
                }
                signalRequired = false;
                String logMessage = null;
                while ((logMessage = messgeQueue.poll()) != null) {
                    byte[] msgByte = (new Date().toString() + ":" + logMessage).getBytes();
                    if (buffer.remaining() < msgByte.length) {
                        logMessage();
                    }
                    buffer.put(msgByte);
                }
                logMessage();

            }
            return null;
        }

    }

    public void info(String message) {
        messgeQueue.add(message + "\n");
        if (signalRequired) {
            messagePublished();
        }
    }

    private void messagePublished() {
        lock.lock();
        try {
            messageCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void waitForMessage() throws InterruptedException {
        lock.lock();
        try {
            messageCondition.await(10, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    public void logMessage() throws IOException {
        buffer.flip();
        fc.write(buffer);
        buffer.clear();
    }

    public void shutdownNow() {
        this.endLogging = true;
        messagePublished();
        try {
            ft.get();
        } catch (Exception e) {
        }
        exe.shutdown();
    }
}
