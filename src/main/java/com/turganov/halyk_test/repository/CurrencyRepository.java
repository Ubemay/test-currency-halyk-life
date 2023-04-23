package com.turganov.halyk_test.repository;

import com.turganov.halyk_test.model.R_CURRENCY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<R_CURRENCY, Long> {

    List<R_CURRENCY> findByDate(LocalDate date);

    List<R_CURRENCY> findByDateAndCode(LocalDate date, String code);
}
