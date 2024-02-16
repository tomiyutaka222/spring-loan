package com.example.presentation.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDto {
    // ユーザ名
    private String userName;
    // 借入総額
    private String totalBorrowingAmount;
    // 年利
    private String annualInterest;
    // 手数料
    private String commission;
    // 返済開始日
    private String repaymentStartDate;
    // 返済終了日
    private String repaymentEndDate;
}
