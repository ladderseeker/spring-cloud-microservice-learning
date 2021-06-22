package com.xiao.microservices.limitsservice.controllers;

import com.xiao.microservices.limitsservice.Configuration;
import com.xiao.microservices.limitsservice.beans.Limit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {

    private final Configuration configuration;

    public LimitsController(Configuration configuration) {
        this.configuration = configuration;
    }

    @GetMapping("/limits")
    public Limit retrieveLimits() {
        return new Limit(configuration.getMinimum(), configuration.getMaximum());
    }

}
