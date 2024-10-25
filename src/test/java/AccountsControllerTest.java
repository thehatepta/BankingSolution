import org.bankingsolution.Contollers.AccountsController;
import org.bankingsolution.Services.AccountsService;
import org.bankingsolution.db.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountsService accountsService;

    @InjectMocks
    private AccountsController accountsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountsController).build();
    }

    @Test
    void testGetAllAccounts() throws Exception {
        Account account = new Account();
        account.setName("Sample Account");
        when(accountsService.findAllAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sample Account"));

        verify(accountsService, times(1)).findAllAccounts();
    }

    @Test
    void testGetAllAccountsEmpty() throws Exception {
        when(accountsService.findAllAccounts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(accountsService, times(1)).findAllAccounts();
    }

    @Test
    void testCreateAccount() throws Exception {
        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Account\"}"))
                .andExpect(status().isCreated());

        verify(accountsService, times(1)).createAccount(any(Account.class));
    }

    @Test
    void testGetAccountFound() throws Exception {
        Account account = new Account();
        account.setName("Existing Account");
        when(accountsService.getAccount(1)).thenReturn(account);

        mockMvc.perform(get("/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Existing Account"));

        verify(accountsService, times(1)).getAccount(1);
    }

    @Test
    void testGetAccountNotFound() throws Exception {
        when(accountsService.getAccount(1)).thenReturn(null);

        mockMvc.perform(get("/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(accountsService, times(1)).getAccount(1);
    }

    @Test
    void testDepositFunds() throws Exception {
        mockMvc.perform(post("/accounts/1/deposit")
                        .param("amount", "100"))
                .andExpect(status().isOk());

        verify(accountsService, times(1)).depositFunds(1, BigDecimal.valueOf(100));
    }

    @Test
    void testWithdrawFunds() throws Exception {
        mockMvc.perform(post("/accounts/1/withdraw")
                        .param("amount", "50"))
                .andExpect(status().isOk());

        verify(accountsService, times(1)).withdrawFunds(1, BigDecimal.valueOf(50));
    }

    @Test
    void testTransferFunds() throws Exception {
        mockMvc.perform(post("/accounts/1/transfer/2")
                        .param("amount", "30"))
                .andExpect(status().isOk());

        verify(accountsService, times(1)).transferFunds(1, 2, BigDecimal.valueOf(30));
    }
}
