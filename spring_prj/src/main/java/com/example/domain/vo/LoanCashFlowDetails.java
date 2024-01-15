package com.example.domain.domainservice.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCashFlowDetails {
    // 返済回数
    private int repaymentNumber;
    // 返済残高
    private BigDecimal repaymentBalance;
    // 返済利息
    private BigDecimal repaymentInterest;
    // 返済金額
    private BigDecimal repaymentAmount;
    // 元本返済金額
    private BigDecimal principalRepaymentAmount;
    // 返済年月日
    private LocalDate repaymentDate;
    // 月利
    private BigDecimal monthlyInterest;
}
