package streams.database;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableStreamBuilder<T> {

    static Pattern columnNamePattern = Pattern.compile("set([A-Z].*)([A-Z].*)");

    private final DataSource ds;
    private Class<T> tableClass;
    private String tableName;
    private Supplier<T> rowSupplier;
    private List<ColumnMetaData> columnNames;

    public TableStreamBuilder(DataSource ds) {
        this.ds = ds;
    }

    public Stream<T> stream() {

        List<Object> pipeLine = new ArrayList<>();

        Stream<T> dataStream = new NoOpStream<T>() {

            @Override
            public Stream<T> filter(Predicate<? super T> predicate) {
                pipeLine.add(predicate);
                return this;
            }

            @Override
            public Stream<T> limit(long maxSize) {
                pipeLine.add(new SQLLimit(maxSize));
                return this;
            }

            @Override
            public long count() {

                StringBuilder where = new StringBuilder();

                decodePipelineStage(where, pipeLine);

                String sql = String.format("SELECT COUNT(1) FROM %s WHERE %s", tableName, where);
                try (Connection con = openConnection(); ResultSet rs = executeQuery(sql, con)) {

                    while (rs.next()) {
                        return rs.getLong(1);
                    }
                    return 0;

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public <R, A> R collect(Collector<? super T, A, R> collector) {
                A container = collector.supplier().get();

                String cols = columnNames
                        .stream()
                        .map(col -> col.columnName)
                        .collect(Collectors.joining(","));

                StringBuilder where = new StringBuilder();
                decodePipelineStage(where, pipeLine);

                String sql = String.format("SELECT %s FROM %s WHERE %s", cols, tableName, where);
                System.out.println(sql);

                try (Connection con = openConnection(); ResultSet rs = executeQuery(sql, con)) {

                    while (rs.next()) {
                        T row = buildRow(rs);
                        collector.accumulator().accept(container, row);
                    }

                } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }


                return collector.finisher().apply(container);
            }

        };

        return dataStream;
    }

    private Connection openConnection() throws SQLException {
        return ds.getConnection();
    }

    private ResultSet executeQuery(String sql, Connection con) throws SQLException {
        return con.createStatement().executeQuery(sql);
    }

    private T buildRow(ResultSet rs) throws SQLException, IllegalAccessException, InvocationTargetException {
        T row = rowSupplier.get();
        int index = 0;
        while (index < columnNames.size()) {
            ColumnMetaData columnMetaData = columnNames.get(index);
            Object value = rs.getObject(index + 1);
            if (!rs.wasNull()) {
                columnMetaData.method().invoke(row, value);
            }
            index++;
        }
        return row;
    }

    private void decodePipelineStage(StringBuilder where, List<Object> pipeLine) {
        for (Object stage : pipeLine) {

            if (isPredicate(stage)) {
                processPredicate(where, (Predicate<T>) stage);
            } else if (isLimit(stage)) {
                SQLLimit limit = (SQLLimit) stage;
                where.append(" LIMIT " + limit.maxSize + " ");
            }
        }
    }

    private boolean isLimit(Object stage) {
        return (stage instanceof SQLLimit);
    }

    private void processPredicate(StringBuilder where, Predicate<T> stage) {
        Predicate predicate = stage;

        if (predicate instanceof Where.GreaterThan) {

            Where.GreaterThan gt = (Where.GreaterThan) predicate;
            if (where.length() > 0) {
                where.append(" AND ");
            }
            where.append(String.format("%s > %s", gt.columnName(), gt.value()));
        }
    }

    private boolean isPredicate(Object stage) {
        return stage instanceof Predicate;
    }

    public TableStreamBuilder<T> build(Class<T> row, String tableName, Supplier<T> supplier) {

        this.tableClass = row;
        this.tableName = tableName;
        this.rowSupplier = supplier;
        this.columnNames = identifyColumnNames(row);

        return this;
    }

    private static List<TableStreamBuilder.ColumnMetaData> identifyColumnNames(Class<?> row) {
        return Arrays.asList(row.getMethods()).stream()
                .filter(method -> method.getName().startsWith("set"))
                .map(method -> new TableStreamBuilder.ColumnMetaData(method, columnNamePattern.matcher(method.getName())))
                .filter(x -> x.matcher.matches())
                .map(x -> x.build())
                .collect(Collectors.toList());
    }

    public static TableStreamBuilder with(DataSource ds) {
        return new TableStreamBuilder(ds);
    }

    static class ColumnMetaData {

        final Method setMethod;
        String columnName;
        final Matcher matcher;

        ColumnMetaData(Method methodRef, Matcher matcher) {
            this.setMethod = methodRef;
            this.matcher = matcher;
        }

        public Method method() {
            return setMethod;
        }

        public String columnName() {
            return columnName;
        }

        public ColumnMetaData build() {
            columnName = matcher.group(1).toLowerCase() + "_" + matcher.group(2).toLowerCase();
            return this;
        }
    }

    private static class SQLLimit {
        private final long maxSize;

        public SQLLimit(long maxSize) {
            this.maxSize = maxSize;
        }
    }
}
