package io.portfel.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

@AllArgsConstructor
public class OptionalResultSetExtractor<T> implements ResultSetExtractor<Optional<T>> {

    private final RowMapper<T> rowMapper;

    @Override
    public Optional<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<T> results = (new ArrayList<>());
        int rowNum = 0;
        while (rs.next()) {
            results.add(this.rowMapper.mapRow(rs, rowNum++));
        }
        if ( CollectionUtils.isEmpty(results)) {
            return Optional.empty();
        }
        if ( results.size() != 1 ) {
            throw new RuntimeException( "Too many entities!" );
        }
        return Optional.of(results.get( 0 ));
    }
}
