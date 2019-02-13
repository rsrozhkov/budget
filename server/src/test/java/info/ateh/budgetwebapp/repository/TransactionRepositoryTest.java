package info.ateh.budgetwebapp.repository;


import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.entity.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TransactionRepository transactionRepository;

    private Member testMember;
    private Long testMemberId;

    @Before
    public void setUp() {
        testMember = new Member("TestMemberName");
        testMemberId = (Long) entityManager.persistAndGetId(testMember);
    }

    @Test
    public void findAll() {
        List<Transaction> result = transactionRepository.findAll();
        assertThat(result).isNotNull();
        assertThat(result).doesNotContainNull();
    }

    @Test
    public void findWithdrawBetweenDates() {
        Date start = new Date(250000000);
        Date end = new Date(450000000);
        Date middle = new Date(350000000);

        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction positiveTransaction = new Transaction(testMember,100L,"Any");
        positiveTransaction.setDate(middle);
        entityManager.persist(positiveTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction negativeTransaction = new Transaction(testMember,-100L,"Any");
        negativeTransaction.setDate(middle);
        entityManager.persist(negativeTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).contains(negativeTransaction);
    }

    @Test
    public void findWithdrawBetweenSwapDates() {
        Date end = new Date(250000000);
        Date start = new Date(450000000);
        Date middle = new Date(350000000);

        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction positiveTransaction = new Transaction(testMember,100L,"Any");
        positiveTransaction.setDate(middle);
        entityManager.persist(positiveTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction negativeTransaction = new Transaction(testMember,-100L,"Any");
        negativeTransaction.setDate(middle);
        entityManager.persist(negativeTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();
    }

    @Test
    public void findWithdrawBetweenDatesWithNullStart() {
        Date start = null;
        Date end = new Date(450000000);
        Date middle = new Date(350000000);

        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction positiveTransaction = new Transaction(testMember,100L,"Any");
        positiveTransaction.setDate(middle);
        entityManager.persist(positiveTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction negativeTransaction = new Transaction(testMember,-100L,"Any");
        negativeTransaction.setDate(middle);
        entityManager.persist(negativeTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();
    }

    @Test
    public void findWithdrawBetweenDatesWithNullEnd() {
        Date start = new Date(250000000);
        Date end = null;
        Date middle = new Date(350000000);

        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction positiveTransaction = new Transaction(testMember,100L,"Any");
        positiveTransaction.setDate(middle);
        entityManager.persist(positiveTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();

        Transaction negativeTransaction = new Transaction(testMember,-100L,"Any");
        negativeTransaction.setDate(middle);
        entityManager.persist(negativeTransaction);
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end)).isEmpty();
    }

    @Test
    public void findByMemberId() {
        List<Transaction> result = transactionRepository.findByMemberId(testMemberId);
        assertThat(result).isEmpty();
        Transaction transactionOne  = new Transaction(testMember,100L,"Any");
        Transaction transactionTwo  = new Transaction(testMember,100L,"Any");
        entityManager.persist(transactionOne);
        entityManager.persist(transactionTwo);
        result = transactionRepository.findByMemberId(testMemberId);
        assertThat(result).contains(transactionOne,transactionTwo);
    }

    @Test
    public void findByNullMemberId() {
        List<Transaction> result = transactionRepository.findByMemberId(null);
        assertThat(result).isEmpty();
    }

    @Test
    public void findByZeroMemberId() {
        List<Transaction> result = transactionRepository.findByMemberId(0L);
        assertThat(result).isEmpty();
    }

    @Test
    public void findByNegativeMemberId() {
        List<Transaction> result = transactionRepository.findByMemberId(-1L);
        assertThat(result).isEmpty();
    }

    @Test
    public void save() {
        Transaction transaction = new Transaction(testMember,100L,"Any");
        assertThat(transactionRepository.findByMemberId(testMemberId)).isEmpty();
        transactionRepository.save(transaction);
        assertThat(transactionRepository.findByMemberId(testMemberId)).contains(transaction);
    }

    @Test
    public void saveSame() {
        Transaction transaction = new Transaction(testMember,100L,"Any");
        transactionRepository.save(transaction);
        transactionRepository.save(transaction);
        assertThat(transactionRepository.findByMemberId(testMemberId).size()).isEqualTo(1);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveNull() {
        transactionRepository.save(null);
    }

}
