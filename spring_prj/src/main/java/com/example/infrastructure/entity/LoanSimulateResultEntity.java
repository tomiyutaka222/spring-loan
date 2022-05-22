package com.example.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * アカウントの情報を永続化するEntity
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_simulate_result")
public class LoanSimulateResultEntity {
    // アカウントのID
    @Id
    @GeneratedValue
    private int id;

    // ユーザの名前
    @Column(nullable = false)
    private String userName;

    // 総返済回数
    @Column(nullable = false)
    private String repaymentNumber;

    // 総借入金額
    @Column(nullable = false)
    private String totalAmount;

    // 総返済利息
    @Column(nullable = false)
    private String totalInterestPaymentAmount;

    // 総返済額
    @Column(nullable = false)
    private String totalPaymentAmount;

    // 年利
    @Column(nullable = false)
    private String interestRate;

}
