package com.example.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.domain.dto.request.LoanRequestDto;
import com.example.domain.dto.response.LoanResponseDto;
import com.example.domain.service.PrincepalAndInterestLoanService;

/**
 * アカウント情報に対してのリクエストを処理します
 */
@RestController
@RequestMapping("PrincepalAndInterest")
@CrossOrigin
public class LoanCalculateController {
    // Beenからserviceクラスを呼び出し
    @Autowired
    PrincepalAndInterestLoanService princepalAndInterestLoanService;

    /**
     * アカウント情報を全件取得します
     * 
     * @return List<AccountEntity> アカウント情報リスト
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public LoanResponseDto calculatePrincepalAndInterestLoan(LoanRequestDto loanRequest) {
        return princepalAndInterestLoanService.calculatePrincepalAndInterestLoan(loanRequest);
    }
}
