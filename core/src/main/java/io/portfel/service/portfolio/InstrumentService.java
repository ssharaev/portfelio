package io.portfel.service.portfolio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.portfel.exception.NotFoundException;
import io.portfel.model.Instrument;
import io.portfel.model.Price;
import io.portfel.repository.InstrumentRepository;
import io.portfel.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final PriceRepository priceRepository;
    private final InstrumentRepository instrumentRepository;

    public List<Price> getPrices(String ticker) {
        return priceRepository.getPrices( ticker, LocalDate.of( 2022, 2, 1 ) );
    }

    public Price getPriceByDay(String ticker, LocalDate date) {
        return priceRepository.getPriceByDay( ticker, date )
            .orElseThrow( () -> new NotFoundException( Price.class, ticker ) );
    }

    public List<Instrument> findInstruments(String ticker) {
        return instrumentRepository.findInstruments( ticker );
    }

    public Optional<Instrument> getById(UUID id) {
        return instrumentRepository.getById( id );
    }

    public Optional<Instrument> getByTicker(String ticker) {
        return instrumentRepository.getByTicker( ticker );
    }
}
