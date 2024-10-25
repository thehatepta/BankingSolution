package org.bankingsolution.Services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bankingsolution.Services.Interfaces.AccountsServiceInterface;
import org.bankingsolution.db.entity.Account;
import org.bankingsolution.db.entity.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountsService implements AccountsServiceInterface {

    private static final Logger logger = LogManager.getLogger(AccountsService.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AccountsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findByStatus((short) 1);
    }

    @Override
    public void createAccount(Account account) {
        accountRepository.save(account);
        logger.info("Account '{}' created with balance: {}", account.getName(), account.getBalance());
    }

    @Override
    public Account getAccount(int id) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findById(id));
        account.ifPresentOrElse(
                acc -> logger.info("Returning account details for ID {}", id),
                () -> logger.warn("Account with ID {} not found", id)
        );
        return account.orElse(null);
    }

    @Override
    public void depositFunds(int id, BigDecimal amount) {
        Account account = findAccountById(id);
        if (account != null) {
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
            logger.info("Deposited {} to account '{}'", amount, account.getName());
        }
    }

    @Override
    public void withdrawFunds(int id, BigDecimal amount) {
        Account account = findAccountById(id);
        if (account != null) {
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            logger.info("Withdrew {} from account '{}'", amount, account.getName());
        }
    }

    @Override
    public void transferFunds(int sourceAccountId, int targetAccountId, BigDecimal amount) {
        Account sourceAccount = findAccountById(sourceAccountId);
        Account targetAccount = findAccountById(targetAccountId);
        if (sourceAccount != null && targetAccount != null) {
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);
            logger.info("Transferred {} from account '{}' to account '{}'", amount, sourceAccount.getName(), targetAccount.getName());
        }
    }

    private Account findAccountById(int id) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findById(id));
        if (account.isEmpty()) {
            logger.warn("Account with ID {} not found", id);
        }
        return account.orElse(null);
    }
}
