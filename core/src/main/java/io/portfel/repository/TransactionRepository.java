package io.portfel.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import io.portfel.model.ChartValueData;
import io.portfel.model.Transaction;
import io.portfel.model.TransactionStatus;
import io.portfel.model.TransactionType;
import io.portfel.model.TransactionsByDay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRepository implements Repo<Transaction> {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_TRANSACTIONS_BY_ID = "SELECT * FROM trasactions where accountId = ?";

    private static final String CREATE_TRANSACTION = "insert into transactions " +
        "(id, type, status, currency, accountId, instrumentId, quantity, fee, total, timestamp) " +
        "VALUES (?,?,?,?,?,?,?,?,?,?)";

    private static final String GET_TRANSACTIONS_BY_DAY = "select timestamp,  " +
        "sum( " +
        "        CASE " +
        "    WHEN (t.type = 'BUY' or t.type = 'INITIAL') THEN t.total " +
        "    ELSE - t.total " +
        "END) as amount, " +
        " " +
        " sum( " +
        "         CASE " +
        "    WHEN (t.type = 'BUY' or t.type = 'INITIAL') THEN t.quantity " +
        "    ELSE - t.quantity " +
        "END) as count, " +
        " " +
        " from transactions t " +
        " where  " +
        " t.instrumentId = ? " +
        " AND t.accountId = ? " +
        " AND t.total is not null " +
        " AND t.status = 'SUCCESS' " +
        " SAMPLE BY 1d;";


    private static final String GET_INSTRUMENT_POSITION =
            "with sampledPortfolio as ( " +
            "        with portfolio as ( " +
            "         " +
            "                with trCte as ( " +
            "                        select t.timestamp,  " +
            "                                sum(CASE " +
            "                                        WHEN (t.type = 'BUY' or t.type = 'INITIAL') THEN t.quantity " +
            "                                        ELSE - t.quantity " +
            "                                        END) count, " +
            "                                sum(CASE " +
            "                                        WHEN (t.type = 'BUY' or t.type = 'INITIAL') THEN t.total " +
            "                                        ELSE - t.total " +
            "                                        END) total " +
            " " +
            "                        from transactions t " +
            "                        WHERE " +
            "                                t.status = 'SUCCESS' " +
            "                                and t.accountId = ? " +
            "                                and t.ticker = ? " +
            "                        ) " +
            "        select timestamp, count, sum(count) OVER ( " +
            "                ORDER BY timestamp " +
            "                RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) as cumulative_count, " +
            "                sum(total) OVER ( " +
            "                ORDER BY timestamp " +
            "                RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) as cumulative_total " +
            "                from trCte) " +
            "select timestamp, sum(cumulative_count) count, sum(cumulative_total) total from portfolio timestamp(timestamp) sample by ? fill(PREV)) " +
            " " +
            "SELECT sp.timestamp, sp.count, sp.total, nasdaq_open_close.Close * sp.count current_val, nasdaq_open_close.Close " +
            "from  " +
            "sampledPortfolio sp " +
            " join nasdaq_open_close on sp.timestamp = nasdaq_open_close.timestamp and nasdaq_open_close.ticker = ? " +
            "where sp.timestamp BETWEEN ? AND ?";

    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
        RowMapper<Transaction> rowMapper = getMapper();
        return jdbcTemplate.query(
            GET_TRANSACTIONS_BY_ID,
            new RowMapperResultSetExtractor<>( rowMapper ),
            accountId.toString()
        );
    }

    public List<TransactionsByDay> getTransactionsByDay(UUID accountId, UUID instrumentId) {
        RowMapper<TransactionsByDay> rowMapper = (rs, rowNum) ->
            new TransactionsByDay(
                rs.getTimestamp( "timestamp" ).toLocalDateTime().toLocalDate(),
                rs.getBigDecimal( "amount" ),
                rs.getBigDecimal( "count" )
            );
        return jdbcTemplate.query(
            GET_TRANSACTIONS_BY_DAY,
            new RowMapperResultSetExtractor<>( rowMapper ),
            instrumentId.toString(), accountId.toString()
        );
    }

    public List<ChartValueData> getPositionHistoryData(UUID accountId, String ticker, String sampleBy, LocalDate from,
                                                       LocalDate to) {
        RowMapper<ChartValueData> rowMapper = (rs, rowNum) ->
            new ChartValueData(
                rs.getTimestamp( "timestamp" ).toLocalDateTime().toLocalDate(),
                rs.getBigDecimal( "count" ),
                rs.getBigDecimal( "currentTotal" ),
                rs.getBigDecimal( "totalSpent" )
            );
        return jdbcTemplate.query(
            GET_INSTRUMENT_POSITION,
            new RowMapperResultSetExtractor<>( rowMapper ),
            accountId.toString(),
            ticker,
            sampleBy,
            ticker,
            from,
            to
        );
    }

    public void saveTransaction(Transaction transaction) {
        jdbcTemplate.update(
            CREATE_TRANSACTION,
            transaction.getId().toString(),
            transaction.getType().toString(),
            transaction.getStatus().toString(),
            transaction.getCurrency(),
            transaction.getAccountId().toString(),
            transaction.getInstrumentId().toString(),
            transaction.getQuantity().doubleValue(),
            transaction.getFee().doubleValue(),
            transaction.getTotal().doubleValue(),
            transaction.getDateTime()
        );
    }

    public RowMapper<Transaction> getMapper() {
        return (rs, rowNum) ->
            new Transaction(
                UUID.fromString( rs.getString( "id" ) ),
                UUID.fromString( rs.getString( "accountId" ) ),
                UUID.fromString( rs.getString( "instrumentId" ) ),
                TransactionType.valueOf( rs.getString( "type" ) ),
                TransactionStatus.valueOf( rs.getString( "status" ) ),
                rs.getString( "currency" ),
                rs.getBigDecimal( "quantity" ),
                rs.getBigDecimal( "fee" ),
                rs.getBigDecimal( "total" ),
                rs.getTimestamp( "timestamp" ).toLocalDateTime()
            );
    }


}
