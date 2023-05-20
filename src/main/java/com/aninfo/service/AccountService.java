package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Transaction nuevaTransaccion = new Transaction();
        nuevaTransaccion.setAccount(account);
        nuevaTransaccion.setAmount(sum);
        nuevaTransaccion.setDate(java.sql.Date.valueOf(LocalDate.now()));
        nuevaTransaccion.setType("withdraw");

        account.addTransaction(nuevaTransaccion);
        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        return account;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        Account account = accountRepository.findAccountByCbu(cbu);

        Transaction nuevaTransaccion = new Transaction();
        nuevaTransaccion.setAccount(account);
        nuevaTransaccion.setAmount(sum);
        nuevaTransaccion.setDate(java.sql.Date.valueOf(LocalDate.now()));
        nuevaTransaccion.setType("deposit");

        account.addTransaction(nuevaTransaccion);

        account.setBalance(account.getBalance() + sum);
        accountRepository.save(account);

        return account;
    }

//    public Optional<Transaction> searchTransaction(Long cbu, Long transactionId) {
//        Account account = accountRepository.findAccountByCbu(cbu);
//        transactiontRepository.findById(transactionId);
//        List<Transaction> transactions = account.getTransactions();
//
//        for (Transaction transaction : transactions) {
//            if (transaction.getId().equals(transactionId)) {
//                return ResponseEntity.ok(transaction);
//            }
//        }
//    }
}
