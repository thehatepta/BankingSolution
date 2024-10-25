package org.bankingsolution.Contollers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bankingsolution.Services.AccountsService;
import org.bankingsolution.db.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private static final Logger logger = LogManager.getLogger(AccountsController.class);

    private final AccountsService accountsService;

    @Autowired
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountsService.findAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createAccount(@RequestBody Account account) {
        accountsService.createAccount(account);
        logger.info("Account {} created", account.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable int id) {
        Account account = accountsService.getAccount(id);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> depositFunds(@PathVariable int id, @RequestParam BigDecimal amount) {
        accountsService.depositFunds(id, amount);
        logger.info("Deposited {} to account with ID {}", amount, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawFunds(@PathVariable int id, @RequestParam BigDecimal amount) {
        accountsService.withdrawFunds(id, amount);
        logger.info("Withdrew {} from account with ID {}", amount, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sourceId}/transfer/{targetId}")
    public ResponseEntity<Void> transferFunds(
            @PathVariable int sourceId,
            @PathVariable int targetId,
            @RequestParam BigDecimal amount) {
        accountsService.transferFunds(sourceId, targetId, amount);
        logger.info("Transferred {} from account {} to account {}", amount, sourceId, targetId);
        return ResponseEntity.ok().build();
    }
}
