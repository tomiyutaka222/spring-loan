package com.example.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.domain.domainservice.calculate.calculator.PrincepalAndInterestLoanCalculater;
import com.example.domain.domainservice.calculate.condition.calculateCondition;
import com.example.domain.domainservice.calculate.vo.PrincepalAndInterestLoanResult;
import com.example.domain.dto.request.LoanRequestDto;
import com.example.domain.dto.response.LoanResponseDto;
import com.example.domain.repository.CashFlowRepository;
import com.example.domain.repository.LoanSimulateResultRepository;
import com.example.infrastructure.entity.CashFlowEntity;
import com.example.infrastructure.entity.LoanSimulateResultEntity;

/**
 * アカウント情報を操作するビジネスロジックを実装します
 */
@Service
@Transactional
public class PrincepalAndInterestLoanService {

    // Beenからリポジトリの呼び出し
    @Autowired
    LoanSimulateResultRepository loanSimulateResultRepository;

    // Beenからリポジトリの呼び出し
    @Autowired
    CashFlowRepository cashFlowRepository;

    public LoanResponseDto calculatePrincepalAndInterestLoan(LoanRequestDto loanRequest) {
        // RequestDTOから計算ロジック用Conditionにデータを詰める
        calculateCondition condition = new calculateCondition(
            new BigDecimal(
                loanRequest.getTotalBorrowingAmount().orElseThrow(()->{
                    throw new IllegalArgumentException("総借入金額が未入力です。");
                })
            ),
            new BigDecimal(
                loanRequest.getAnnualInterest().orElseThrow(()->{
                    throw new IllegalArgumentException("年利が未入力です。");
                })
            ).divide(BigDecimal.valueOf(100)),
            new BigDecimal(
                loanRequest.getCommission().orElseThrow(()->{
                    throw new IllegalArgumentException("手数料が未入力です。");
                })
            ),
            LocalDate.parse(
                loanRequest.getRepaymentStartDate().orElseThrow(()->{
                    throw new IllegalArgumentException("返済開始日が未入力です。");
                })
            ),
            LocalDate.parse(
                loanRequest.getRepaymentEndDate().orElseThrow(()->{
                    throw new IllegalArgumentException("返済完了日が未入力です。");
                })
            )
            
        );

        // Calculatorを呼び出して計算を行う
        PrincepalAndInterestLoanResult calculateResult = new PrincepalAndInterestLoanCalculater().calculate(condition);

        // ローンシミュレートテーブルに保存するデータを作成する
        List<LoanSimulateResultEntity> loanSimulateResultEntity = Arrays.asList(
            new LoanSimulateResultEntity(
                0,
                loanRequest.getUserName().get(),
                String.valueOf(calculateResult.getRepaymentNumber()),
                loanRequest.getTotalBorrowingAmount().get().toString(),
                calculateResult.getTotalInterestPaymentAmount().toString(),
                calculateResult.getTotalPaymentAmount().toString(),
                loanRequest.getAnnualInterest().get().toString()
            )
        );

        // Entityに登録する
        List<LoanSimulateResultEntity> saveLoanSimulateResultData = loanSimulateResultRepository.saveAll(loanSimulateResultEntity);

        // リストを定義
        List<CashFlowEntity> cashFlowEntity = new ArrayList<CashFlowEntity>();

        // キャッシュフローをエンティティに詰める
        for(int i = 0;i < 600;i++) {
            cashFlowEntity.add(
                new CashFlowEntity(
                    0,
                    saveLoanSimulateResultData.get(0).getId(),
                    calculateResult.getPrincepalAndInterestLoanDetails().get(i).getRepaymentNumber(),
                    calculateResult.getPrincepalAndInterestLoanDetails().get(i).getRepaymentDate().toString(),
                    calculateResult.getPrincepalAndInterestLoanDetails().get(i).getRepaymentAmount().toString(),
                    calculateResult.getPrincepalAndInterestLoanDetails().get(i).getPrincipalRepaymentAmount().toString(),
                    calculateResult.getPrincepalAndInterestLoanDetails().get(i).getMonthlyInterest().toString(),
                    calculateResult.getPrincepalAndInterestLoanDetails().get(i).getRepaymentBalance().toString(),
                    calculateResult.getPrincepalAndInterestLoanDetails().get(i).getRepaymentInterest().toString()
                ));
        }

        // キャッシュフローテーブルに登録する
        cashFlowRepository.saveAll(cashFlowEntity);

        // DTOに詰めかえて返却する
        return new LoanResponseDto(
            loanRequest.getUserName().get(),
            calculateResult.getRepaymentNumber(),
            calculateResult.getTotalPaymentAmount(),
            calculateResult.getTotalInterestPaymentAmount(),
            calculateResult.getPrincepalAndInterestLoanDetails()
        );
    }


}