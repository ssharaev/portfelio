package io.portfel.web;

import java.math.BigDecimal;
import java.util.UUID;

import io.portfel.model.Account;
import io.portfel.model.Transaction;
import io.portfel.service.portfolio.AccountService;
import io.portfel.web.dto.AccountDto;
import io.portfel.web.dto.CreateAccountDto;
import io.portfel.web.dto.CreateTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.emptyList;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping(path = "/{id}")
    public AccountDto getAccount(@PathVariable UUID id) {
        Account account = accountService.getAccountBy( id );
        return new AccountDto( account.getId().toString(), account.getAccountType(), BigDecimal.TEN, emptyList() );
    }

    @PostMapping
    public AccountDto createAccount(@RequestBody CreateAccountDto createAccountDto) {
        Account account = accountService.createAccount( createAccountDto.getUserId(), createAccountDto.getType() );
        return new AccountDto( account.getId().toString(), account.getAccountType(), BigDecimal.TEN, emptyList() );
    }

    @PostMapping(path = "/{id}/transactions")
    public Transaction createTransaction(@PathVariable UUID id, @RequestBody CreateTransactionDto request) {
        Transaction transaction = accountService.createTransaction( id,
            request.getInstrumentId(),
            request.getTransactionType(),
            request.getTransactionStatus(),
            request.getCurrency(),
            request.getQuantity(),
            request.getFee(),
            request.getTotal(),
            request.getDateTime()
        );
        return transaction;
    }
}
