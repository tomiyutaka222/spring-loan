package com.example.domain.domainservice.calculate.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class PrincepalAndInterestLoanProcessorTest {

    @Test
    void testCalcAnnualInterest() {
        int expected = new PrincepalAndInterestLoanProcessor().calcRepaymentNumber(LocalDate.of(2022, 5, 2),
                LocalDate.of(2072, 4, 2));
        assertEquals(expected, 600);
    }

    @Test
    void testCalcPmt() {
        BigDecimal expected = new PrincepalAndInterestLoanProcessor().calcPmt(BigDecimal.valueOf(0.0157), 600,
                BigDecimal.valueOf(30000000));
        assertEquals(expected, BigDecimal.valueOf(72197));

    }

    @Test
    void testCalcPrincipalRepaymentAmount() {
        BigDecimal expected = new PrincepalAndInterestLoanProcessor().calcPrincipalRepaymentAmount(
                BigDecimal.valueOf(0.0157), BigDecimal.valueOf(100),
                BigDecimal.valueOf(72147), 600, 0);
        System.out.println(expected);
    }

    @Test
    void testCalcRepaymentAmount() {
        BigDecimal expected = new PrincepalAndInterestLoanProcessor().calcRepaymentAmount(
            BigDecimal.valueOf(30000000),
            BigDecimal.valueOf(300000)
        );

        assertEquals(expected, BigDecimal.valueOf(30300000));

    }

    @Test
    void testCalcRepaymentBalance() {
        BigDecimal expected = new PrincepalAndInterestLoanProcessor().calcRepaymentBalance(BigDecimal.valueOf(30000000),
                BigDecimal.valueOf(32947), 600, 0);
        assertEquals(expected, BigDecimal.valueOf(29967053));

    }

    @Test
    void testCalcRepaymentDate() {
        LocalDate expected = new PrincepalAndInterestLoanProcessor().calcRepaymentDate(LocalDate.of(2022, 5, 2),
                LocalDate.of(2022, 5, 2), 1);
        assertEquals(expected, LocalDate.of(2022, 5, 2));
    }

    @Test
    void testCalcRepaymentMonthlyInterest() {
        BigDecimal mounthlyRate = BigDecimal.valueOf(0.0157).divide(BigDecimal.valueOf(12), 50, RoundingMode.DOWN);
        BigDecimal expected = new PrincepalAndInterestLoanProcessor().calcRepaymentMonthlyInterest(mounthlyRate,
                BigDecimal.valueOf(30000000));
        assertEquals(expected, BigDecimal.valueOf(39249));
    }
}
