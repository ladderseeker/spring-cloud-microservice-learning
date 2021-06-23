package com.example.currencyconversionservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    private final static String uri = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";

    private final CurrencyExchangeProxy currencyExchangeProxy;

    public CurrencyConversionController(CurrencyExchangeProxy currencyExchangeProxy) {
        this.currencyExchangeProxy = currencyExchangeProxy;
    }

    @GetMapping({"/currency-converter/from/{from}/to/{to}/quantity/{quantity}"})
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("from", from);
        uriMap.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = restTemplate.getForEntity(uri, CurrencyConversion.class, uriMap);

        CurrencyConversion responseBody = responseEntity.getBody();

        return getCurrencyConversion(from, to, quantity, responseBody);
    }

    @GetMapping({"/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}"})
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        CurrencyConversion responseBody = currencyExchangeProxy.retrieveExchangeValue(from, to);

        CurrencyConversion currencyConversion = getCurrencyConversion(from, to, quantity, responseBody);
        currencyConversion.setEnvironment(currencyConversion.getEnvironment() + " :feign");

        return currencyConversion;
    }

    private CurrencyConversion getCurrencyConversion(String from, String to, BigDecimal quantity, CurrencyConversion responseBody) {
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setId(responseBody.getId());
        currencyConversion.setFrom(from);
        currencyConversion.setTo(to);
        currencyConversion.setConversionMultiple(responseBody.getConversionMultiple());
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculateAmount(quantity.multiply(responseBody.getConversionMultiple()));
        currencyConversion.setEnvironment(responseBody.getEnvironment());

        return currencyConversion;
    }

}
