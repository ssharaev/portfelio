package io.portfel.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.portfel.model.Instrument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InstrumentRepository implements Repo<Instrument> {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_INSTRUMENTS = " SELECT * from instruments where ticker like ? OR name like ?";
    private static final String GET_BY_ID = " SELECT * from instruments where id = ?";
    private static final String GET_BY_TICKER = " SELECT * from instruments where ticker = ?";

    public List<Instrument> findInstruments(String filter) {
        return jdbcTemplate.query( FIND_INSTRUMENTS, new RowMapperResultSetExtractor<>( getMapper()),
            filter + "%",
            "%" + filter + "%");
    }

    public Optional<Instrument> getById(UUID id) {
        return jdbcTemplate.query( GET_BY_ID,  new OptionalResultSetExtractor<>( getMapper() ), id.toString());
    }

    public Optional<Instrument> getByTicker(String ticker) {
        return jdbcTemplate.query( GET_BY_TICKER,  new OptionalResultSetExtractor<>( getMapper() ), ticker);
    }


    @Override
    public RowMapper<Instrument> getMapper() {
        RowMapper<Instrument> rowMapper = (rs, rowNum) ->
            new Instrument(
                UUID.fromString(rs.getString( "id" )),
                rs.getString( "ticker" ),
                rs.getString( "name" ),
                UUID.fromString(rs.getString( "exchangeId" )),
                rs.getTimestamp( "timestamp" ).toLocalDateTime());
        return rowMapper;
    }
}
