package com.aninfo.repository;

import com.aninfo.model.Transaction;
import com.aninfo.model.Account;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TransactionRepository extends CrudRepository<Transaction, Long>{

    //Transaction findTransactionByid(Long id);
    //List<Transaction> findAllByCbu(Long cbu);

    //Optional<Transaction> findById(Long id);

    List<Transaction> findByAccount(Account account);

}
