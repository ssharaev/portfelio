package io.portfel.web;

import java.time.LocalDate;
import java.util.List;

import io.portfel.model.Instrument;
import io.portfel.model.Price;
import io.portfel.service.portfolio.InstrumentService;
import io.portfel.web.dto.PriceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @GetMapping(path = "{ticker}/prices")
    public List<PriceDto> getPrices(@PathVariable String ticker) {
        List<Price> priceList = instrumentService.getPrices(ticker);
        log.info( "Found {} prices!", priceList.size() );
        return priceList.stream()
            .map( price -> new PriceDto( price.getTicker(), price.getPrice(), price.getDateTime() ) )
            .toList();

    }

    @GetMapping(path = "{ticker}/prices/{date}")
    public PriceDto getPriceByDate(@PathVariable String ticker, @PathVariable LocalDate date ) {
        Price price = instrumentService.getPriceByDay( ticker, date );
        return  new PriceDto( price.getTicker(), price.getPrice(), price.getDateTime() );

    }

    @GetMapping(path = "find/{ticker}")
    public List<Instrument> findByTicker(@PathVariable String ticker) {
        List<Instrument> instrumentList = instrumentService.findInstruments(ticker);
        log.info( "Found {} instruments!", instrumentList.size() );
        return instrumentList;

    }
}
