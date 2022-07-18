package com.example.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import com.example.domain.dto.request.LoanRequestDto;
import com.example.domain.dto.response.LoanResponseDto;
import com.example.domain.service.PrincepalAndInterestLoanService;

/**
 * アカウント情報に対してのリクエストを処理します
 */
@Controller
public class LoanCalculateController {
    // Beenからserviceクラスを呼び出し
    @Autowired
    PrincepalAndInterestLoanService princepalAndInterestLoanService;

    @RequestMapping("/")
	public ModelAndView index(ModelAndView mav) {
        /* ModelAndViewClassを使用し、Viewへ渡すデータを作成する */
        // 現在のURIを格納
		mav.addObject("currentUri", "/");

		// 使用するビューを設定
		mav.setViewName("index"); // (3)

        
        // 画面に上で作ったデータを返す
		return mav;
	}

    /**
     * アカウント情報を全件取得します
     * 
     * @return List<AccountEntity> アカウント情報リスト
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public ModelAndView calculatePrincepalAndInterestLoan(@RequestBody MultiValueMap loanRequest, ModelAndView mav) {
        // インプットをDTOに格納する
        LoanRequestDto loanRequestDto = new LoanRequestDto(
            // ユーザ名
            Optional.of(loanRequest.toSingleValueMap().get("userName").toString()),
            // 総返済額
            Optional.of(loanRequest.toSingleValueMap().get("totalBorrowingAmount").toString()),
            // 設定年利
            Optional.of(loanRequest.toSingleValueMap().get("annualInterest").toString()),
            // 手数料
            Optional.of(loanRequest.toSingleValueMap().get("commission").toString()),
            // 返済開始日
            Optional.of(loanRequest.toSingleValueMap().get("repaymentStartDate").toString()),
            // 返済終了日
            Optional.of(loanRequest.toSingleValueMap().get("repaymentEndDate").toString())
        );

        // 上で作ったDTOを引数に渡して、サービスクラスを呼び出す
        LoanResponseDto loanResponseDto = princepalAndInterestLoanService.calculatePrincepalAndInterestLoan(loanRequestDto);
        
        /* ModelAndViewClassを使用し、Viewへ渡すデータを作成する */
        // 現在のURIを格納
		mav.addObject("currentUri", "/calculate");
        // 計算結果DTOを格納
		mav.addObject("loanResponseDto", loanResponseDto);

		// 使用するビューを設定
		mav.setViewName("index"); // (3)
        
        // 画面に上で作ったデータを返す
        return mav;
    }
}
