package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // TODO バリデーションの実施
        // RequestDTOから計算ロジック用Conditionにデータを詰める
        // TODO 金利をBigDecimalからFloat型に変換する
        calculateCondition condition = new calculateCondition(
            new BigDecimal(loanRequest.getTotalBorrowingAmount()),
            new BigDecimal(loanRequest.getAnnualInterest()).divide(BigDecimal.valueOf(100)),
            new BigDecimal(loanRequest.getCommission()),
            LocalDate.parse(loanRequest.getRepaymentStartDate()),
            LocalDate.parse(loanRequest.getRepaymentEndDate())
        );

        // Calculatorを呼び出して計算を行う
        PrincepalAndInterestLoanResult calculateResult = new PrincepalAndInterestLoanCalculater().calculate(condition);

        // ローンシミュレートテーブルに保存するデータを作成する
        Optional<List<LoanSimulateResultEntity>> loanSimulateResultEntity = Optional.ofNullable(
                Arrays.asList(
                    new LoanSimulateResultEntity(
                        0,
                        loanRequest.getUserName(),
                        String.valueOf(calculateResult.getRepaymentNumber()),
                        loanRequest.getTotalBorrowingAmount().toString(),
                        calculateResult.getTotalInterestPaymentAmount().toString(),
                        calculateResult.getTotalPaymentAmount().toString(),
                        loanRequest.getAnnualInterest().toString()
                    )
                )
        );
        
        // Entityに登録する
        List<LoanSimulateResultEntity> saveLoanSimulateResultData = loanSimulateResultEntity.map(e -> loanSimulateResultRepository.saveAll(e)).get();

        // キャッシュフローをエンティティに詰める
        List<CashFlowEntity> cashFlowEntity = calculateResult.getLoanCashFlowDetails().stream().map(e -> { return new CashFlowEntity(
                    0,
                    saveLoanSimulateResultData.get(0).getId(),
                    e.getRepaymentNumber(),
                    e.getRepaymentDate().toString(),
                    e.getRepaymentAmount().toString(),
                    e.getPrincipalRepaymentAmount().toString(),
                    e.getMonthlyInterest().toString(),
                    e.getRepaymentBalance().toString(),
                    e.getRepaymentInterest().toString()
        );}).collect(Collectors.toList());

        // キャッシュフローテーブルに登録する
        cashFlowRepository.saveAll(cashFlowEntity);

        // DTOに詰めかえて返却する
        return new LoanResponseDto(
            loanRequest.getUserName(),
            calculateResult.getRepaymentNumber(),
            calculateResult.getTotalPaymentAmount(),
            calculateResult.getTotalInterestPaymentAmount(),
            calculateResult.getLoanCashFlowDetails()
        );
    }


}