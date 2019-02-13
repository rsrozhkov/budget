package info.ateh.budgetwebapp.service;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.entity.Transaction;
import info.ateh.budgetwebapp.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    @TestConfiguration
    static class TransactionServiceTestContextConfiguration {
        @Bean
        public TransactionService transactionServiceTest() {
            return new TransactionService();
        }
    }

    @Autowired
    TransactionService service;

    @MockBean
    private TransactionRepository repository;

    @MockBean
    private Member member;

    private Transaction transactionOne;
    private Transaction transactionTwo;
    private List<Transaction> transactions;
    private List<Transaction> foundBetweenDates;
    private Date start;
    private Date end;
    private Long memberIdOne;
    private Long memberIdTwo;

    @Before
    public void setUp() {
        transactionOne = new Transaction(member,300L,"Any");
        transactionTwo = new Transaction(member,-100L,"Any");
        transactions = new ArrayList<>();
        transactions.add(transactionOne);
        transactions.add(transactionTwo);
        foundBetweenDates = new ArrayList<>();
        foundBetweenDates.add(transactionTwo);
        start = new Date(250000000);
        end = new Date(450000000);
        memberIdOne = 1L;
        memberIdTwo = 2L;

        Mockito.when(repository.findAll()).thenReturn(transactions);
        Mockito.when(repository.findWithdrawBetweenDates(start,end)).thenReturn(foundBetweenDates);
        Mockito.when(repository.findByMemberId(memberIdOne)).thenReturn(transactions);
        Mockito.when(repository.findByMemberId(memberIdTwo)).thenReturn(new ArrayList<>());
        Mockito.when(repository.save(transactionOne)).thenReturn(transactionOne);
    }

    @Test
    public void getBalance() {
        Long balance = transactionOne.getAmount()+transactionTwo.getAmount();
        assertThat(service.getBalance()).isEqualTo(balance);
    }

    @Test
    public void getAllTransactions() {
        assertThat(service.getAllTransactions()).isEqualTo(transactions);
    }

    @Test
    public void getWithdrawBetweenDates() {
        List<Transaction> result = service.getWithdrawBetweenDates(start,end);
        assertThat(result).isEqualTo(foundBetweenDates);
    }

    @Test
    public void checkMemberForTransactions() {
        assertTrue(service.checkMemberForTransactions(memberIdOne));
        assertFalse(service.checkMemberForTransactions(memberIdTwo));
    }

    @Test
    public void addTransaction() {
        Transaction result = service.addTransaction(transactionOne);
        assertThat(result).isEqualTo(transactionOne);
    }

    @Test(expected = NullPointerException.class)
    public void addNullTransaction() {
        service.addTransaction(null);
    }

    @Test
    public void normalizeSpaces() {
        transactionOne.setComment("  Any     comment  ");
        Transaction result = service.addTransaction(transactionOne);
        assertThat(result.getComment()).isEqualTo("Any comment");
    }
}
