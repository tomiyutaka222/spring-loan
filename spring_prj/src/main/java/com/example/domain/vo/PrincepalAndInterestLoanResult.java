package com.example.domain.vo;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrincepalAndInterestLoanResult {
    // 総支払回数
    private int repaymentNumber;
    // 総支払金額
    private BigDecimal totalPaymentAmount;
    // 総支払利息
    private BigDecimal totalInterestPaymentAmount;
    // 支払キャッシュフロー
    private List<LoanCashFlowDetails> LoanCashFlowDetails;
}
