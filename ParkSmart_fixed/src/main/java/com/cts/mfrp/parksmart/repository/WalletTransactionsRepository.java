package com.cts.mfrp.parksmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.mfrp.parksmart.model.Users;
import com.cts.mfrp.parksmart.model.WalletTransactions;

public interface WalletTransactionsRepository extends JpaRepository<WalletTransactions, Integer> {
    List<WalletTransactions> findByUserOrderByTimestampDesc(Users user);
}
