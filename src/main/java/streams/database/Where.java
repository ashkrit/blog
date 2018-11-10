package streams.database;

import java.util.function.Predicate;

public class Where<T> {

    enum Operator {
        GT
    }

    public static Predicate GT(String col, int value) {
        return new GreaterThan(Operator.GT, col, value);
    }

    public static Predicate GT(String col, double value) {
        return new GreaterThan(Operator.GT, col, value);
    }

    public static abstract class WherePredicate<T> implements Predicate<T> {

        private final Operator op;
        private final String columnName;

        WherePredicate(Operator op, String columnName) {
            this.op = op;
            this.columnName = columnName;
        }

        public Operator Op() {
            return op;
        }

        public String columnName() {
            return columnName;
        }
    }

    public static class GreaterThan<T> extends WherePredicate<T> {

        private final Object value;

        GreaterThan(Operator op, String columnName, Object value) {
            super(op, columnName);
            this.value = value;
        }

        public Object value() {
            return value;
        }

        @Override
        public boolean test(T t) {
            return false;
        }
    }

}
