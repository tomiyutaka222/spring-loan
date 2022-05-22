package com.example.domain.domainservice.calculate.condition;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class calculateCondition {
    // 借入総額
    private BigDecimal totalBorrowingAmount;
    // 年利
    private BigDecimal annualInterest;
    // 手数料
    private BigDecimal commission;
    // 返済開始日
    private LocalDate repaymentStartDate;
    // 返済終了日
    private LocalDate repaymentEndDate;
}
