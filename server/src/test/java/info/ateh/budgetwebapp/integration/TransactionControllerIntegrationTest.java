package info.ateh.budgetwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.ateh.budgetwebapp.BudgetWebApplication;
import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.entity.Transaction;
import info.ateh.budgetwebapp.service.MemberService;
import info.ateh.budgetwebapp.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static info.ateh.budgetwebapp.utils.Constants.DATE_PATTERN;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BudgetWebApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    TransactionService transactionService;

    @Test
    public void balance() throws Exception{
        Long balance = transactionService.getBalance();
        mvc.perform(get("/transactions/balance")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(balance));
    }

    @Test
    public void all() throws Exception{
        List<Transaction> all = transactionService.getAllTransactions();
        mvc.perform(get("/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(all.get(0).getId()));
    }

    @Test
    public void withdrawBetweenDates() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        String startDate = dateFormat.format(new Date(0));
        String endDate = dateFormat.format(new Date());

        mvc.perform(get("/transactions/withdrawBetweenDates/{start}/{end}", startDate, endDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void checkForOwner() throws Exception{
        mvc.perform(get("/transactions/checkForOwner/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void newTransaction() throws Exception{
        Member member = memberService.getById(1L);
        Transaction transaction = new Transaction(member,1000L,"Any");
        transaction = transactionService.addTransaction(transaction);
        String jsonTransaction = mapper.writeValueAsString(transaction);

        mvc.perform(post("/transactions/")
                .content(jsonTransaction)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transaction.getId()));
    }

    @Test
    public void newTransactionEmpty() throws Exception{
        mvc.perform(post("/transactions/")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test (expected = ConstraintViolationException.class)
    public void newTransactionNullMember() throws Exception{
        Transaction transaction = new Transaction(null,1000L,"Any");
        transaction = transactionService.addTransaction(transaction);
        String jsonTransaction = mapper.writeValueAsString(transaction);

        mvc.perform(post("/transactions/")
                .content(jsonTransaction)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test (expected = InvalidDataAccessApiUsageException.class)
    public void newTransactionNotExistMember() throws Exception{
        Member member = new Member("Any");
        Transaction transaction = new Transaction(member,1000L,"Any");
        transaction = transactionService.addTransaction(transaction);
        String jsonTransaction = mapper.writeValueAsString(transaction);

        mvc.perform(post("/transactions/")
                .content(jsonTransaction)
                .contentType(MediaType.APPLICATION_JSON));
    }

}
