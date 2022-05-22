package com.example.domain.domainservice.calculate.calculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.domain.domainservice.calculate.condition.calculateCondition;
import com.example.domain.domainservice.calculate.processor.PrincepalAndInterestLoanProcessor;
import com.example.domain.domainservice.calculate.vo.PrincepalAndInterestLoanDetails;
import com.example.domain.domainservice.calculate.vo.PrincepalAndInterestLoanResult;


public class PrincepalAndInterestLoanCalculater {

        /**
         * 元利均等型返済のキャッシュフローを作成します
         * 
         * @param condition
         * @return
         */
        public PrincepalAndInterestLoanResult calculate(calculateCondition condition) {

                PrincepalAndInterestLoanProcessor princepalAndInterestLoanProcessor = new PrincepalAndInterestLoanProcessor();

                // 日付から総返済回数を計算する
                int repaymentNumber = princepalAndInterestLoanProcessor.calcRepaymentNumber(
                                // 返済開始日
                                condition.getRepaymentStartDate(),
                                // 返済終了日
                                condition.getRepaymentEndDate());

                // 年利から月利を計算する
                BigDecimal monthlyRate = princepalAndInterestLoanProcessor.calcAnnualInterest(
                                // 年利
                                condition.getAnnualInterest());

                // PMT値を計算する
                BigDecimal pmt = princepalAndInterestLoanProcessor.calcPmt(
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
                List<PrincepalAndInterestLoanDetails> details = new ArrayList<PrincepalAndInterestLoanDetails>();

                // 返済回数分処理を繰り返す
                for (int number = 1; number <= repaymentNumber; number++) {
                        // 今回の返済年月日を計算する
                        repaymentDate = princepalAndInterestLoanProcessor.calcRepaymentDate(
                                        // 返済開始日
                                        condition.getRepaymentStartDate(),
                                        // 現在の返済日
                                        repaymentDate,
                                        // 今回の返済回数
                                        number);

                        // 返済残高から今月の利息金額を計算する
                        monthlyRepaymentInterest = princepalAndInterestLoanProcessor
                                        .calcRepaymentMonthlyInterest(monthlyRate, repaymentBalance);

                        // PMT値と返済利息から今月の元本返済金額を計算する
                        principalRepaymentAmount = princepalAndInterestLoanProcessor.calcPrincipalRepaymentAmount(
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
                        repaymentBalance = princepalAndInterestLoanProcessor.calcRepaymentBalance(
                                        // 現在の返済残高
                                        repaymentBalance,
                                        // 今回の返済元本
                                        principalRepaymentAmount,
                                        // 総返済回数
                                        repaymentNumber,
                                        // 今回の返済回数
                                        number);

                        // 返済金額を計算する
                        repaymentAmount = princepalAndInterestLoanProcessor.calcRepaymentAmount(principalRepaymentAmount, monthlyRepaymentInterest);

                        // Detailsにキャッシュフローを追加する
                        details.add( new PrincepalAndInterestLoanDetails(
                                                        number,
                                                        repaymentBalance,
                                                        condition.getAnnualInterest(),
                                                        repaymentAmount,
                                                        principalRepaymentAmount,
                                                        repaymentDate,
                                                        monthlyRepaymentInterest));
                }
                
                // 総支払利息
                BigDecimal totalInterestPaymentAmount = princepalAndInterestLoanProcessor
                                .calcTotalInterestPaymentAmount(details);

                // 総支払金額
                BigDecimal totalPaymentAmount = princepalAndInterestLoanProcessor
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
