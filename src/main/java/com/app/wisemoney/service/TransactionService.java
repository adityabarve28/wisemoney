package com.app.wisemoney.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.wisemoney.entity.TransactionEntity;
import com.app.wisemoney.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionEntity saveTransaction(TransactionEntity transaction) {
        return transactionRepository.save(transaction);
    }

    public void updateTransaction(TransactionEntity transaction) {
        TransactionEntity existingTransaction = transactionRepository.findById(transaction.getTransactionId())
            .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transaction.getTransactionId()));
        existingTransaction.setTitle(transaction.getTitle());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setDate(transaction.getDate());
        existingTransaction.setDescription(transaction.getDescription());
        transactionRepository.save(existingTransaction);
    }

    public List<TransactionEntity> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public double getTotalAmountSpentByUserId(Long userId) {
        List<TransactionEntity> transactions = getTransactionsByUserId(userId);
        return transactions.stream().mapToDouble(TransactionEntity::getAmount).sum();
    }


    public double getTotalAmountSpentInCurrentMonth(Long userId) {
        List<TransactionEntity> transactions = getTransactionsByUserId(userId);
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        return transactions.stream()
            .filter(t -> {
                LocalDate transactionDate = t.getDate().toLocalDate();
                return !transactionDate.isBefore(startOfMonth) && !transactionDate.isAfter(endOfMonth);
            })
            .mapToDouble(TransactionEntity::getAmount)
            .sum();
    }

    public void deleteTransaction(Long transactionId) {
        TransactionEntity transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));
        transactionRepository.delete(transaction);
    }
}
