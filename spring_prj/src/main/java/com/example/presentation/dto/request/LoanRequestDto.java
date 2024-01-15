package com.example.presentation.dto.request;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDto {
    // ユーザ名
    private Optional<String> userName;
    // 借入総額
    private Optional<String> totalBorrowingAmount;
    // 年利
    private Optional<String> annualInterest;
    // 手数料
    private Optional<String> commission;
    // 返済開始日
    private Optional<String> repaymentStartDate;
    // 返済終了日
    private Optional<String> repaymentEndDate;
}
