package com.example.domain.repository;

import com.example.infrastructure.entity.LoanSimulateResultEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * アカウント情報EntityにアクセスするRepositoryクラス
 */
@Repository
public interface LoanSimulateResultRepository extends JpaRepository<LoanSimulateResultEntity, Integer> {
}