package streams;

import org.apache.commons.dbcp2.BasicDataSource;
import streams.database.TableStreamBuilder;
import streams.database.Where;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableStreamApplication {

    public static void main(String... args) {

        BasicDataSource ds = createDataSource();

        TableStreamBuilder stocksTable = TableStreamBuilder
                .with(ds)
                .build(StocksPrice.class, "stocks_price", () -> new StocksPrice());

        Stream<StocksPrice> rows = stocksTable.stream();

        long count = rows
                .filter(Where.GT("volume", 1467200))
                .filter(Where.GT("open_price", 1108d))
                .count();

        System.out.println(count);


        List<StocksPrice> o = (List<StocksPrice>) stocksTable.stream()
                .filter(Where.GT("volume", 1467200))
                .filter(Where.GT("open_price", 1108d))
                .limit(2)
                .collect(Collectors.toList());

        System.out.println(o);


    }

    private static BasicDataSource createDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/playground");
        ds.setUsername("root");
        ds.setPassword("changeme");
        return ds;
    }

}
