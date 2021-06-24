package com.xiao.microservices.currencyexchangeservice;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CircuitBreakerController {

    @GetMapping("/sample-api")
//    @Retry(name = "sample-api", fallbackMethod = "hardcodedResponse")
    @CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
//    @RateLimiter(name = "default") // 10s => 10000 calls
    @Bulkhead(name = "sample-api")
    public String sampleApi() {

        log.info("Try to access dummy-api");

//        RestTemplate template = new RestTemplate();
//        ResponseEntity<String> forEntity = template.getForEntity("http://localhost:8080/dummy-api", String.class);

//        return forEntity.getBody();
        return "Sample-api";
    }

    public String hardcodedResponse(Exception e) {
        return "fallback-response";
    }
}
