package com.aninfo;

import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.service.AccountService;
import com.aninfo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@SpringBootApplication
@EnableSwagger2
public class Memo1BankApp {

	@Autowired
	private AccountService accountService;
	private TransactionService transactionService;

	public static void main(String[] args) {
		SpringApplication.run(Memo1BankApp.class, args);
	}

	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public Account createAccount(@RequestBody Account account) {
		return accountService.createAccount(account);
	}

	@GetMapping("/accounts")
	public Collection<Account> getAccounts() {
		return accountService.getAccounts();
	}

	@GetMapping("/accounts/{cbu}")
	public ResponseEntity<Account> getAccount(@PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);
		return ResponseEntity.of(accountOptional);
	}

	@GetMapping("/accounts/{cbu}/transactions")
	public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);
		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();
			List<Transaction> transactions = account.getTransactions();
			//List<Transaction> transactions = transactionService.findAllByAccount(account);
			return ResponseEntity.ok(transactions);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/accounts/{cbu}/transactions/{transactionId}")
	public ResponseEntity<Transaction> getTransactionById(@PathVariable Long cbu ,@PathVariable Long transactionId) {

		Optional<Account> accountOptional = accountService.findById(cbu);

		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();
//			accountService.searchTransaction(account,transactionId);
//
			List<Transaction> transactions = account.getTransactions();

			for (Transaction transaction : transactions) {
				if (transaction.getId().equals(transactionId)) {
					return ResponseEntity.ok(transaction);
				}
			}
			return ResponseEntity.notFound().build();

		}
		return ResponseEntity.notFound().build();
	}
	@PutMapping("/accounts/{cbu}")
	public ResponseEntity<Account> updateAccount(@RequestBody Account account, @PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);

		if (!accountOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		account.setCbu(cbu);
		accountService.save(account);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/accounts/{cbu}")
	public void deleteAccount(@PathVariable Long cbu) {
		accountService.deleteById(cbu);
	}

	@PutMapping("/accounts/{cbu}/withdraw")
	public Account withdraw(@PathVariable Long cbu, @RequestParam Double sum) {
		return accountService.withdraw(cbu, sum);
	}

	@PutMapping("/accounts/{cbu}/deposit")
	public Account deposit(@PathVariable Long cbu, @RequestParam Double sum) {
		return accountService.deposit(cbu, sum);
	}

	@GetMapping("/accounts/{cbu}/transactions-v2")
	public ResponseEntity<List<Transaction>> getAccountTransaction(@PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);
		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();
			List<Transaction> transactionOptional = transactionService.findAllByAccount(account);
			return ResponseEntity.of(Optional.ofNullable(transactionOptional));
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/accounts/transactions-v2/{transactionId}")
	public ResponseEntity<Transaction> getTransaction(@PathVariable Long transactionId) {
		Optional<Transaction> transactionOptional = transactionService.findById(transactionId);
		return ResponseEntity.of(transactionOptional);
	}

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.any())
			.build();
	}
}
