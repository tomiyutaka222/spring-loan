package com.example.domain.domainservice.processor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.example.domain.vo.LoanCashFlowDetails;

/**
 * ローン計算の計算式を実装します
 */
public class PrincepalAndInterestLoanProcessor {

    /**
     * 日付から総返済回数を計算して返却する
     * 
     * @param startDate 返済開始日
     * @param endDate   返済終了日
     * @return 総返済回数
     */
    public static int calcRepaymentNumber(LocalDate startDate, LocalDate endDate) {
        return Math.toIntExact(ChronoUnit.MONTHS.between(startDate, endDate)) + 1;
    }

    /**
     * 年利から月利を計算して返却する
     * 
     * @param interestRate 年利
     * @return 月利
     */
    public static BigDecimal calcAnnualInterest(BigDecimal interestRate) {
        return interestRate.divide(BigDecimal.valueOf(12), 40, BigDecimal.ROUND_DOWN);
    }

    /**
     * PMT値を計算して返却する
     * 
     * @param interestRate     月利
     * @param repaymentNumber 総返済回数  返済回数
     * @param repaymentBalance 返済残高 借入金額
     * @return PMT値 元本返済額
     */
    public static BigDecimal calcPmt(BigDecimal interestRate, int repaymentNumber, BigDecimal repaymentBalance) {
        return PmtProcessor.pmtCalculate(interestRate, repaymentNumber, repaymentBalance);
    }

    /**
     * 返済残高から今月の利息金額を計算して返却する
     * 
     * @param interestRate 年利
     * @param repaymentBalance 返済残高 借入金額
     * @return 利息金額 元本返済額
     */
    public static BigDecimal calcRepaymentMonthlyInterest(BigDecimal interestRate, BigDecimal repaymentBalance) {
        return repaymentBalance.multiply(interestRate).setScale(0, RoundingMode.DOWN);
    }

    /**
     * PMT値と返済利息から今月の元本返済金額を計算して返却する
     * 
     * @param monthlyRepaymentInterest 月利
     * @param repaymentBalance 返済残高 総返済額
     * @param pmt PMT値 元本返済額
     * @param repaymentNumber 総返済回数
     * @param number 現在の返済回数
     * @return 元本返済金額
     */
    public static BigDecimal calcPrincipalRepaymentAmount(BigDecimal monthlyRepaymentInterest, BigDecimal repaymentBalance, BigDecimal pmt,
            int repaymentNumber, int number) {
        // 返済最終回
        if (repaymentNumber == number) {
            return repaymentBalance;
        }
        // 返済最終回以外
        return pmt.subtract(monthlyRepaymentInterest).setScale(0, RoundingMode.DOWN);
    }

    /**
     * 今回の返済年月日を計算して返却する
     * 
     * @param startDate 返済開始日
     * @param repaymentDate 返済日
     * @param number 現在の返済回数
     * @return 返済年月日
     */
    public static LocalDate calcRepaymentDate(LocalDate startDate, LocalDate repaymentDate, int number) {
        // 返済初回
        if (number == 1) {
            return startDate;
        }
        // 返済初回以外
        return repaymentDate.plusMonths(1);
    }

    /**
     * 元本返済金額から返済残高を計算して返却する
     * 
     * @param repaymentBalance 返済残高
     * @param repaymentAmount 元本返済額
     * @param repaymentNumber 総返済回数
     * @param number 現在の返済回数
     * @return 返済残高
     */
    public static BigDecimal calcRepaymentBalance(BigDecimal repaymentBalance, BigDecimal repaymentAmount, int repaymentNumber,
            int number) {
        // 返済最終回
        if (repaymentNumber == number) {
            return BigDecimal.valueOf(0);
        }
        // 返済最終回以外
        return repaymentBalance.subtract(repaymentAmount);
    }

    /**
     * 総返済利息を計算して返却する
     * 
     * @param details キャッシュフローリスト
     * @return 総返済利息
     */
    public static BigDecimal calcTotalInterestPaymentAmount(List<LoanCashFlowDetails> details) {
        BigDecimal total = BigDecimal.valueOf(
                details.stream().mapToInt(val -> val.getMonthlyInterest().intValue()).sum());
        return total;
    }

    /**
     * 総返済金額を計算して返却する
     * 
     * @param totalBorrowingAmount 総返済金額
     * @param totalInterestPaymentAmount 総返済利息
     * @return 総返済金額
     */
    public static BigDecimal calcTotalBorrowingAmount(BigDecimal totalBorrowingAmount, BigDecimal totalInterestPaymentAmount) {
        return totalBorrowingAmount.add(totalInterestPaymentAmount);
    }

    /**
     * 返済金額を計算して返却する
     * 
     * @param principalRepaymentAmount 返済元本
     * @param monthlyRepaymentInterest
     * @return 返済金額
     */
    public static BigDecimal calcRepaymentAmount(BigDecimal principalRepaymentAmount, BigDecimal monthlyRepaymentInterest) {
        return principalRepaymentAmount.add(monthlyRepaymentInterest);
    }
}
