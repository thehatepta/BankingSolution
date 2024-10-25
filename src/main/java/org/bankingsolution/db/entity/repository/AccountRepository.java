package org.bankingsolution.db.entity.repository;

import org.bankingsolution.db.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findById(int id);

    List<Account> findByStatus(short status);
}
