package org.bankingsolution.Services.Interfaces;

import org.bankingsolution.db.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountsServiceInterface {
    List<Account> findAllAccounts();

    void createAccount(Account account);

    Account getAccount(int id);

    void depositFunds(int id, BigDecimal fundsAdjust);

    void withdrawFunds(int id, BigDecimal fundsAdjust);

    void transferFunds(int firstId, int SecondId, BigDecimal fundsAdjust);
}
