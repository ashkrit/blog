package wraparound;


import java.io.IOException;

public class WrapAround {

    @FunctionalInterface
    public interface CodeBlock {
        void execute();
    }

    @FunctionalInterface
    public interface Condition {
        boolean test();
    }

    public static void loop(Condition condition, CodeBlock codeBlock) {
        while (condition.test()) {
            codeBlock.execute();
        }
    }

    @FunctionalInterface
    public interface AutoCodeBlock {
        void execute(AutoCloseable closeable) throws IOException;
    }

    @FunctionalInterface
    public interface ExceptionBlock {
        void execute() throws Exception;
    }


    public static void time(String name, CodeBlock codeBlock) {
        long start = System.currentTimeMillis();
        codeBlock.execute();
        long total = System.currentTimeMillis() - start;
        System.out.println(name + " took " + total + " ms");
    }

    public static void withAutoClose(AutoCloseable resource, AutoCodeBlock codeBlock) throws Exception {
        try (AutoCloseable c = resource) {
            codeBlock.execute(c);
        }
    }

    public static void wrapWithRuntimeException(ExceptionBlock codeBlock) {
        try {
            codeBlock.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
