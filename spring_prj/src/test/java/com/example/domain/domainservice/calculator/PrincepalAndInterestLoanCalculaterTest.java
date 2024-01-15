package com.example.domain.domainservice.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.domain.condition.calculateCondition;
import com.example.domain.vo.PrincepalAndInterestLoanResult;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PrincepalAndInterestLoanCalculaterTest {

    @Autowired
    PrincepalAndInterestLoanCalculater princepalAndInterestLoanCalculater;

    @Test
    void testCalculate() {
        calculateCondition condition = new calculateCondition(
                BigDecimal.valueOf(30000000),
                BigDecimal.valueOf(0.0157),
                BigDecimal.valueOf(30000),
                LocalDate.of(2022, 5, 1),
                LocalDate.of(2072, 4, 1));
        PrincepalAndInterestLoanResult result = new PrincepalAndInterestLoanCalculater().calculate(condition);

        assertEquals(result.getTotalInterestPaymentAmount(), BigDecimal.valueOf(13318385));
        assertEquals(result.getTotalPaymentAmount(), BigDecimal.valueOf(43318385));
        assertEquals(result.getRepaymentNumber(), 600);
        assertEquals(result.getLoanCashFlowDetails().size(), 600);
        System.out.println(result);
    }
}
