package io.portfel.repository;

import java.util.Optional;
import java.util.UUID;

import io.portfel.model.Account;
import io.portfel.model.AccountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountRepository implements Repo<Account> {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ACCOUNT_BY_ID = "SELECT * FROM accounts where id = ?";
    private static final String INSERT_ACCOUNT_BY_ID = "insert into accounts (id, userId, type, createdAt) values (?, ?, ?, ?)";

    public Optional<Account> getAccount(UUID accountId) {
        return jdbcTemplate.query(
            GET_ACCOUNT_BY_ID,
            new OptionalResultSetExtractor<>( getMapper() ),
            accountId.toString()
        );
    }

    public RowMapper<Account> getMapper() {
        return (rs, rowNum) ->
            new Account(
                UUID.fromString( rs.getString( "id" ) ),
                UUID.fromString( rs.getString( "userId" ) ),
                AccountType.valueOf( rs.getString( "type" ) ),
                rs.getTimestamp( "createdAt" ).toLocalDateTime()
            );
    }

    public void saveAccount(Account account) {
        jdbcTemplate.update(
            INSERT_ACCOUNT_BY_ID,
            account.getId(),
            account.getUserId(),
            account.getAccountType().toString(),
            account.getCreatedAt()
        );
    }


}
