package com.example.domain.domainservice.calculate.calculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.domain.domainservice.calculate.condition.calculateCondition;
import com.example.domain.domainservice.calculate.processor.PrincepalAndInterestLoanProcessor;
import com.example.domain.domainservice.calculate.vo.LoanCashFlowDetails;
import com.example.domain.domainservice.calculate.vo.PrincepalAndInterestLoanResult;

/**
 * 元利均等型ローン計算式の組み立てを実装します
 */
public class PrincepalAndInterestLoanCalculater {

        /**
         * 元利均等型返済のキャッシュフローを作成します
         * 
         * @param condition
         * @return ローン計算結果
         */
        public PrincepalAndInterestLoanResult calculate(calculateCondition condition) {
                // 日付から総返済回数を計算する
                int repaymentNumber = PrincepalAndInterestLoanProcessor.calcRepaymentNumber(
                                // 返済開始日
                                condition.getRepaymentStartDate(),
                                // 返済終了日
                                condition.getRepaymentEndDate());

                // 年利から月利を計算する
                BigDecimal monthlyRate = PrincepalAndInterestLoanProcessor.calcAnnualInterest(
                                // 年利
                                condition.getAnnualInterest());

                // PMT値を計算する
                BigDecimal pmt = PrincepalAndInterestLoanProcessor.calcPmt(
                                // 年利
                                condition.getAnnualInterest(),
                                // 返済回数
                                repaymentNumber,
                                // 総返済残高
                                condition.getTotalBorrowingAmount());

                // 返済残高
                BigDecimal repaymentBalance = condition.getTotalBorrowingAmount();

                // 今回の返済年月日
                LocalDate repaymentDate = condition.getRepaymentStartDate();

                // 返済利息
                BigDecimal monthlyRepaymentInterest = BigDecimal.valueOf(0);

                // 返済総額
                BigDecimal principalRepaymentAmount = BigDecimal.valueOf(0);

                // 
                BigDecimal repaymentAmount = BigDecimal.valueOf(0);

                // キャッシュフローリストを定義
                List<LoanCashFlowDetails> details = new ArrayList<LoanCashFlowDetails>();

                // 返済回数分処理を繰り返す
                for (int number = 1; number <= repaymentNumber; number++) {
                        // 今回の返済年月日を計算する
                        repaymentDate = PrincepalAndInterestLoanProcessor.calcRepaymentDate(
                                        // 返済開始日
                                        condition.getRepaymentStartDate(),
                                        // 現在の返済日
                                        repaymentDate,
                                        // 今回の返済回数
                                        number);

                        // 返済残高から今月の利息金額を計算する
                        monthlyRepaymentInterest = PrincepalAndInterestLoanProcessor
                                        .calcRepaymentMonthlyInterest(monthlyRate, repaymentBalance);

                        // PMT値と返済利息から今月の元本返済金額を計算する
                        principalRepaymentAmount = PrincepalAndInterestLoanProcessor.calcPrincipalRepaymentAmount(
                                        // 今月の返済利息
                                        monthlyRepaymentInterest,
                                        // 現在の返済残高
                                        repaymentBalance,
                                        // PMT値
                                        pmt,
                                        // 総返済回数
                                        repaymentNumber,
                                        // 今回の返済回数
                                        number);
                        
                        

                        // 元本返済金額から返済残高を計算する
                        repaymentBalance = PrincepalAndInterestLoanProcessor.calcRepaymentBalance(
                                        // 現在の返済残高
                                        repaymentBalance,
                                        // 今回の返済元本
                                        principalRepaymentAmount,
                                        // 総返済回数
                                        repaymentNumber,
                                        // 今回の返済回数
                                        number);

                        // 返済金額を計算する
                        repaymentAmount = PrincepalAndInterestLoanProcessor.calcRepaymentAmount(principalRepaymentAmount, monthlyRepaymentInterest);

                        // Detailsにキャッシュフローを追加する
                        details.add( new LoanCashFlowDetails(
                                // 今回の返済回数目
                                number,
                                // 残返済額
                                repaymentBalance,
                                // 適用年利
                                condition.getAnnualInterest(),
                                // 返済総額
                                repaymentAmount,
                                // 返済元本
                                principalRepaymentAmount,
                                // 返済日
                                repaymentDate,
                                // 返済利息 
                                monthlyRepaymentInterest));
                }
                
                // 総支払利息
                BigDecimal totalInterestPaymentAmount = PrincepalAndInterestLoanProcessor
                                .calcTotalInterestPaymentAmount(details);

                // 総支払金額
                BigDecimal totalPaymentAmount = PrincepalAndInterestLoanProcessor
                                .calcTotalBorrowingAmount(condition.getTotalBorrowingAmount(),
                                                totalInterestPaymentAmount);

                // resultに計算結果を詰めて返す
                return new PrincepalAndInterestLoanResult(
                                // 総返済回数
                                repaymentNumber,
                                // 総支払利息
                                totalPaymentAmount,
                                // 総支払金額
                                totalInterestPaymentAmount,
                                // キャッシュフローリスト
                                details);
        }
}
