package com.turganov.halyk_test.controller;

import com.turganov.halyk_test.model.R_CURRENCY;
import com.turganov.halyk_test.repository.CurrencyRepository;
import com.turganov.halyk_test.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRepository currencyRepository;


    @GetMapping("/save/{date}")
    public ResponseEntity<Map<String, Integer>> saveCurrency(@PathVariable String date) throws Exception {
        int count = currencyService.saveCurrency(date);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{date}/{code}")
    public List<R_CURRENCY> getCurrency(@PathVariable LocalDate date, @PathVariable(required = false) String code) {


        return currencyRepository.findByDateAndCode(date, code);

    }

}
