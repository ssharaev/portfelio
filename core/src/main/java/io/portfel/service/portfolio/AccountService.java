package io.portfel.service.portfolio;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import io.portfel.exception.NotFoundException;
import io.portfel.model.Account;
import io.portfel.model.AccountType;
import io.portfel.model.Transaction;
import io.portfel.model.TransactionStatus;
import io.portfel.model.TransactionType;
import io.portfel.repository.AccountRepository;
import io.portfel.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final Clock clock;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Account getAccountBy(UUID id) {
        return accountRepository.getAccount( id )
            .orElseThrow( () -> new NotFoundException( Account.class, id ) );
    }

    public Account createAccount(UUID userId, AccountType type) {
        Account account = new Account( UUID.randomUUID(), userId, type, LocalDateTime.now( clock ) );
        accountRepository.saveAccount( account );
        return account;
    }

    public Transaction createTransaction(UUID accountId,
                                         UUID instrumentId,
                                         TransactionType transactionType,
                                         TransactionStatus transactionStatus,
                                         String currency,
                                         BigDecimal quantity,
                                         BigDecimal fee,
                                         BigDecimal total,
                                         LocalDateTime dateTime
    ) {
        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            accountId,
            instrumentId,
            transactionType,
            transactionStatus,
            currency,
            quantity,
            fee,
            total,
            dateTime
        );
        transactionRepository.saveTransaction( transaction );
        return transaction;
    }

}
