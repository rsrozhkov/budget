package info.ateh.budgetwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.entity.Transaction;
import info.ateh.budgetwebapp.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static info.ateh.budgetwebapp.utils.Constants.DATE_PATTERN;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TransactionService service;

    @Test
    public void balance() throws Exception{
        Long balance = 100L;
        given(service.getBalance()).willReturn(balance);
        mvc.perform(get("/transactions/balance")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(balance));
    }

    @Test
    public void all() throws Exception{
        Transaction transaction = new Transaction(new Member(), 100L, "Any");
        Long id = 1L;
        transaction.setId(id);
        List<Transaction> all = Arrays.asList(transaction);
        given(service.getAllTransactions()).willReturn(all);
        mvc.perform(get("/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id));
    }

    @Test
    public void withdrawBetweenDates() throws Exception{
        Transaction transaction = new Transaction();
        Long id = 1L;
        transaction.setId(id);
        List<Transaction> transactions = Arrays.asList(transaction);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        String startDate = dateFormat.format(new Date());
        String endDate = dateFormat.format(new Date());

        given(service.getWithdrawBetweenDates(dateFormat.parse(startDate),dateFormat.parse(endDate))).willReturn(transactions);

        mvc.perform(get("/transactions/withdrawBetweenDates/{start}/{end}", startDate, endDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id));
    }

    @Test
    public void checkForOwner() throws Exception{
        Long id = 1L;
        given(service.checkMemberForTransactions(id)).willReturn(true);
        mvc.perform(get("/transactions/checkForOwner/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkForOwnerNull() throws Exception{
        Long id = null;
        mvc.perform(get("/transactions/checkForOwner/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void newTransaction() throws Exception{
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        String jsonTransaction = mapper.writeValueAsString(transaction);
        given(service.addTransaction(transaction)).willReturn(transaction);
        mvc.perform(post("/transactions/")
                .content(jsonTransaction)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.date", is(transaction.getDate())));
    }
}
