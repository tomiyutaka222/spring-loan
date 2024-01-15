package com.example.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.domain.domainservice.calculator.PrincepalAndInterestLoanCalculater;
import com.example.domain.condition.calculateCondition;
import com.example.domain.vo.PrincepalAndInterestLoanResult;
import com.example.presentation.dto.request.LoanRequestDto;
import com.example.presentation.dto.response.LoanResponseDto;
import com.example.infrastructure.repository.CashFlowRepository;
import com.example.infrastructure.repository.LoanSimulateResultRepository;
import com.example.infrastructure.entity.CashFlowEntity;
import com.example.infrastructure.entity.LoanSimulateResultEntity;

/**
 * アカウント情報を操作するビジネスロジックを実装します
 */
@Service
@Transactional
public class PrincepalAndInterestLoanService {

    // Beenからローン計算結果永続化の為のリポジトリの呼び出し
    @Autowired
    LoanSimulateResultRepository loanSimulateResultRepository;

    // Beenからキャッシュフロー永続化の為のリポジトリの呼び出し
    @Autowired
    CashFlowRepository cashFlowRepository;

    /**
     * 元利均等ローン計算のユースケースを実装します
     * ・計算インプットの作成
     * ・計算処理
     * ・データ永続化
     * 
     * @param loanRequest
     * @return ローン計算の結果
     */
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
        // キャッシュフローをエンティティに詰める
        for(int i = 0;i < calculateResult.getLoanCashFlowDetails().size();i++) {
            cashFlowEntity.add(
                new CashFlowEntity(
                    0,
                    saveLoanSimulateResultData.get(0).getId(),
                    calculateResult.getLoanCashFlowDetails().get(i).getRepaymentNumber(),
                    calculateResult.getLoanCashFlowDetails().get(i).getRepaymentDate().toString(),
                    calculateResult.getLoanCashFlowDetails().get(i).getRepaymentAmount().toString(),
                    calculateResult.getLoanCashFlowDetails().get(i).getPrincipalRepaymentAmount().toString(),
                    calculateResult.getLoanCashFlowDetails().get(i).getMonthlyInterest().toString(),
                    calculateResult.getLoanCashFlowDetails().get(i).getRepaymentBalance().toString(),
                    calculateResult.getLoanCashFlowDetails().get(i).getRepaymentInterest().toString()
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
            calculateResult.getLoanCashFlowDetails()
        );
    }


}