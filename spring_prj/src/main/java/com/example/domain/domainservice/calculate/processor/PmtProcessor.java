package com.example.domain.domainservice.calculate.processor;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * PMTの計算式を実装します
 * =(利率/12)*借入金額/(1-1/(1+利率/12)^(返済期間*12))
 */
public class PmtProcessor {

    /**
     * PMT値を計算します
     * 
     * @param interestRate     年利
     * @param repaymentNumber  返済回数
     * @param repaymentBalance 総返済額
     * @return PMT値
     */
    public static BigDecimal pmtCalculate(BigDecimal interestRate, int repaymentNumber, BigDecimal repaymentBalance) {
        // 月金利を計算する
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), 50, RoundingMode.DOWN);
        // 月利を計算する
        BigDecimal repaymentInterest = monthlyRate.multiply(repaymentBalance);
        // 
        BigDecimal decimal = BigDecimal.valueOf(1)
                .subtract(BigDecimal.valueOf(1)
                        .add(monthlyRate)
                        .pow(repaymentNumber));

        return (repaymentInterest.subtract(repaymentInterest.divide(decimal, RoundingMode.DOWN)))
                .setScale(0, BigDecimal.ROUND_DOWN);
    }
}
