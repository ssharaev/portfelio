package io.portfel.repository;

import org.springframework.jdbc.core.RowMapper;

public interface Repo<T> {

    RowMapper<T> getMapper();
}
