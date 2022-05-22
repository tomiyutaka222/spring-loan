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
@Table(name = "cash_flow")
public class CashFlowEntity {
    // キャッシュフローのID
    @Id
    @GeneratedValue
    private int id;

    // 登録シミュレートID
    @Column(nullable = false)
    private int simulateId;

    // 返済回数
    @Column(nullable = false)
    private int repaymentNumber;

    // 返済年月日
    @Column(nullable = false)
    private String repaymentDate;
    
    // 返済金額
    @Column(nullable = false)
    private String repaymentAmount;

    // 返済金額
    @Column(nullable = false)
    private String principalRepaymentAmount;

    // 月利
    @Column(nullable = false)
    private String monthlyInterest;
    
    // 返済残高
    @Column(nullable = false)
    private String repaymentBalance;

    // 返済利息
    @Column(nullable = false)
    private String repaymentInterest;

}
