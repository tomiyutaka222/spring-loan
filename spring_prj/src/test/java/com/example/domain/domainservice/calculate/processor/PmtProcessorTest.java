package com.example.domain.domainservice.calculate.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * キャッシュフロー確認のためのテストメソッド
 * ・キャッシュフローのアサーションは未実装
 */
public class PmtProcessorTest {
    @Autowired
    PmtProcessor pmtProcessor;

    @Test
    void testPmtCalculate() {
        BigDecimal pmt = new PmtProcessor().pmtCalculate(BigDecimal.valueOf(0.0157), 600,
                BigDecimal.valueOf(30000000));
        System.out.println(pmt);
        assertEquals(pmt, BigDecimal.valueOf(72197));
    }
}
