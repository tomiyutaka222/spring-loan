package com.example.infrastructure.repository;

import com.example.infrastructure.entity.CashFlowEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * アカウント情報EntityにアクセスするRepositoryクラス
 */
@Repository
public interface CashFlowRepository extends JpaRepository<CashFlowEntity, Integer> {
}
