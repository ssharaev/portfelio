package io.portfel.service.portfolio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.portfel.exception.BaseRuntimeException;
import io.portfel.model.Instrument;
import io.portfel.model.Price;
import io.portfel.model.TransactionsByDay;
import io.portfel.repository.PriceRepository;
import io.portfel.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.decampo.xirr.NewtonRaphson;
import org.decampo.xirr.Transaction;
import org.decampo.xirr.Xirr;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
// TODO naming
public class ProfitCalculationService {

    // TODO wrap in service
    private final TransactionRepository transactionRepository;
    private final PriceRepository priceRepository;
    private final InstrumentService instrumentService;

    // TODO add calculation with date
    public BigDecimal profit(UUID accountId, String ticker, LocalDate from, LocalDate to) {
        Instrument instrument = instrumentService.getByTicker( ticker )
            .orElseThrow( () -> new BaseRuntimeException( "Unable to find instrument with ticker " + ticker ) );
        List<TransactionsByDay> transactionsByDays = transactionRepository.getTransactionsByDay(
            accountId,
            instrument.getId()
        );
        if ( CollectionUtils.isEmpty( transactionsByDays ) ) {
            return BigDecimal.ZERO;
        }
        BigDecimal currentCount = transactionsByDays.stream()
            .map( TransactionsByDay::getCount ).reduce( BigDecimal::add )
            .orElse( BigDecimal.ZERO );
        if ( currentCount.equals( BigDecimal.ZERO ) ) {
            return BigDecimal.ZERO;
        }
        Price lastPrice = priceRepository.getLastPrice( ticker )
            .orElseThrow( () -> new BaseRuntimeException(
                "Unable to find last price with ticker " + ticker ) );

        TransactionsByDay lastTransaction = new TransactionsByDay(
            lastPrice.getDateTime().toLocalDate(),
            currentCount.multiply( lastPrice.getPrice() ).negate(),
            currentCount
        );

        TransactionsByDay todayTransaction = new TransactionsByDay(
            LocalDate.now(),
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );

        List<Transaction> transactions = Stream.concat( transactionsByDays.stream(), Stream.of( lastTransaction, todayTransaction ) )
            .sorted()
            .map( tr -> new Transaction( - tr.getTotal().doubleValue(), tr.getDate() ) )
            .toList();
        log.error( "{}", transactions.stream().map( tr -> String.format( "%.2f - %s",  tr.getAmount(), tr.getWhen().toString() ) ).collect(
            Collectors.joining(";")));
        Xirr xirr = Xirr.builder()
            .withTransactions( transactions )
            .withNewtonRaphsonBuilder( NewtonRaphson.builder().withTolerance( 0.001d ) )
            .build();
        return BigDecimal.valueOf(xirr.xirr());

    }
}
