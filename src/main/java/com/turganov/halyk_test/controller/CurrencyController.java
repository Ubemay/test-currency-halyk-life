package com.turganov.halyk_test.controller;

import com.turganov.halyk_test.exception.ErrorResponse;
import com.turganov.halyk_test.exception.InvalidDateFormatException;
import com.turganov.halyk_test.model.R_CURRENCY;
import com.turganov.halyk_test.repository.CurrencyRepository;
import com.turganov.halyk_test.service.CurrencyService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> saveCurrency(@PathVariable String date) {
        try {
            int count = currencyService.saveCurrency(date);
            Map<String, Integer> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (InvalidDateFormatException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid format date. Correct format is: 24.04.2023");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{date}/{code}")
    public ResponseEntity<Object> getCurrency(@PathVariable LocalDate date, @PathVariable(required = false) String code) {
        try {
            List<R_CURRENCY> result;
            if (code == null) {
                result = currencyRepository.findByDate(date);
            } else {
                result = currencyRepository.findByDateAndCode(date, code);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            JSONObject errorObject = new JSONObject();
            errorObject.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorObject.put("message", "Failed to retrieve currency data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorObject.toString());
        }
    }

}
