package io.portfel.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.portfel.model.Price;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriceRepository implements Repo<Price> {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_PRICES = "SELECT * FROM nasdaq_open_close where Ticker in (?) AND timestamp in (?)";
    private static final String GET_LAST_PRICE = "with cte as( (SELECT Ticker, Timestamp, Close FROM nasdaq_open_close) " +
        "LATEST ON Timestamp PARTITION BY Ticker) " +
        "select Ticker, Timestamp,CLose from cte where Ticker = ?";
    
    private static final String GET_PRICES_FOR_PERIOD = "select " +
        "        timestamp, " +
        "        ticker, " +
        "        last(Close) " +
        "from " +
        "        nasdaq_open_close " +
        "WHERE " +
        "        ticker = ? " +
        "        AND timestamp BETWEEN ? AND ? sample by ? fill(PREV);";

    public List<Price> getPrices(String ticker, LocalDate localDate) {
        RowMapper<Price> rowMapper = getMapper();
        return jdbcTemplate.query( GET_PRICES, new RowMapperResultSetExtractor<>( rowMapper), ticker, localDate);
    }

    public List<Price> getPrices(String ticker, LocalDate from, LocalDate to, String sampledBy) {
        RowMapper<Price> rowMapper = getMapper();
        return jdbcTemplate.query( GET_PRICES_FOR_PERIOD, new RowMapperResultSetExtractor<>( rowMapper), ticker, from, to, sampledBy);
    }

    public Optional<Price> getPriceByDay(String ticker, LocalDate localDate) {
        RowMapper<Price> rowMapper = getMapper();
        return jdbcTemplate.query( GET_PRICES, new OptionalResultSetExtractor<>( rowMapper), ticker, localDate);
    }

    public Optional<Price> getLastPrice(String ticker) {
        return jdbcTemplate.query( GET_LAST_PRICE, new OptionalResultSetExtractor<>( getMapper()), ticker);
    }

    @Override
    public RowMapper<Price> getMapper() {
        return (rs, rowNum) ->
            new Price(
                rs.getString( "Ticker" ),
                rs.getBigDecimal( "Close" ),
                rs.getTimestamp( "Timestamp" ).toLocalDateTime()
            );
    }
}
