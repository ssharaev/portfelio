package io.portfel.service.portfolio;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import io.portfel.model.Price;
import io.portfel.model.chart.ChartRecord;
import io.portfel.model.ChartValueData;
import io.portfel.model.chart.ChartMultiValue;
import io.portfel.repository.PriceRepository;
import io.portfel.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartService {

    private final TransactionRepository transactionRepository;
    private final PriceRepository priceRepository;


    public ChartRecord getPositionGraph(UUID accountId, String ticker, LocalDate from, LocalDate to) {

        List<ChartValueData> positionData = transactionRepository.getPositionHistoryData(
            accountId,
            ticker,
            defineSample( from, to ),
            from,
            to
        );
        List<ChartMultiValue> chartMultiValues = positionData.stream()
            .map( position -> new ChartMultiValue(
                position.getDate(),
                List.of( position.getDepositTotal(), position.getCurrentTotal() )
            ) )
            .toList();
        return new ChartRecord( "Position chart", chartMultiValues, List.of( "deposit", "total amount" ) );
    }

    public ChartRecord getInstrumentGraph(String ticker, LocalDate from, LocalDate to) {

        List<Price> prices = priceRepository.getPrices(
            ticker,
            from,
            to,
            defineSample( from, to )
        );
        List<ChartMultiValue> chartMultiValues = prices.stream()
            .map( position -> new ChartMultiValue(
                position.getDateTime().toLocalDate(),
                List.of( position.getPrice() )
            ) )
            .toList();
        return new ChartRecord( "Instrument chart", chartMultiValues, List.of( "Closed" ) );
    }

    private String defineSample(LocalDate from, LocalDate to) {
        long durationInDays = Duration.between( from, to ).toDays();
        if ( durationInDays < 60 ) {
            return "1d";
        }
        if ( durationInDays < 365 ) {
            return "1w";
        }
        return "1M";
    }
}
