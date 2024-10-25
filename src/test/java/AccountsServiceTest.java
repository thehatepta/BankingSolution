import org.bankingsolution.Services.AccountsService;
import org.bankingsolution.db.entity.Account;
import org.bankingsolution.db.entity.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountsServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountsService accountsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllAccounts() {
        Account account = new Account();
        account.setName("Sample Account");
        account.setStatus((short) 1);

        when(accountRepository.findByStatus((short) 1)).thenReturn(List.of(account));

        List<Account> accounts = accountsService.findAllAccounts();

        assertEquals(1, accounts.size());
        assertEquals("Sample Account", accounts.get(0).getName());
        verify(accountRepository, times(1)).findByStatus((short) 1);
    }

    @Test
    void testCreateAccount() {
        Account account = new Account();
        account.setName("New Account");

        accountsService.createAccount(account);

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testGetAccount() {
        Account account = new Account();
        account.setName("Existing Account");

        when(accountRepository.findById(1)).thenReturn(account);

        Account result = accountsService.getAccount(1);

        assertEquals("Existing Account", result.getName());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void testDepositFunds() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
        when(accountRepository.findById(1)).thenReturn(account);

        accountsService.depositFunds(1, BigDecimal.valueOf(50));

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        assertEquals(BigDecimal.valueOf(150), accountCaptor.getValue().getBalance());
    }

    @Test
    void testWithdrawFunds() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
        when(accountRepository.findById(1)).thenReturn(account);

        accountsService.withdrawFunds(1, BigDecimal.valueOf(50));

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        assertEquals(BigDecimal.valueOf(50), accountCaptor.getValue().getBalance());
    }

    @Test
    void testTransferFunds() {
        Account sourceAccount = new Account();
        sourceAccount.setBalance(BigDecimal.valueOf(200));
        sourceAccount.setName("Source Account");

        Account targetAccount = new Account();
        targetAccount.setBalance(BigDecimal.valueOf(100));
        targetAccount.setName("Target Account");

        when(accountRepository.findById(1)).thenReturn(sourceAccount);
        when(accountRepository.findById(2)).thenReturn(targetAccount);

        accountsService.transferFunds(1, 2, BigDecimal.valueOf(50));

        verify(accountRepository, times(1)).save(sourceAccount);
        verify(accountRepository, times(1)).save(targetAccount);

        assertEquals(BigDecimal.valueOf(150), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(150), targetAccount.getBalance());
    }
}

