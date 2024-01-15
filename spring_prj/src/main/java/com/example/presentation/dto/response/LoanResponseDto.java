package com.example.presentation.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.example.domain.domainservice.calculate.vo.LoanCashFlowDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDto {
    // ユーザ名
    private String userName;
    // 総支払回数
    private int repaymentNumber;
    // 総支払金額
    private BigDecimal totalPaymentAmount;
    // 総支払利息
    private BigDecimal totalInterestPaymentAmount;
    // 支払キャッシュフロー
    private List<LoanCashFlowDetails> loanCashFlowDetails;
}
