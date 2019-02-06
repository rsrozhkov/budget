package info.ateh.budgetwebapp.repository;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.entity.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

//    @Test
//    public void findByMemberId() {
//        //--- Когда репозиторий пуст
//        assertThat(transactionRepository.findByMemberId(1L).size()).isZero();
//
//        Member anka = new Member("Анка");
//        entityManager.persist(anka);
//        entityManager.flush();
//        Transaction transaction = new Transaction(anka,1000L,"Empty");
//        entityManager.persist(transaction);
//        entityManager.flush();
//
//        //--- Когда репозиторий не пуст, но в нём нет нужного члена семьи
//        List<Transaction> notFound = transactionRepository.findByMemberId(anka.getId()+1);
//        assertThat(notFound.size()).isZero();
//
//        //--- Когда в репозитории присутствует нужный член семьи
//        List<Transaction> found = transactionRepository.findByMemberId(anka.getId());
//        assertThat(found.get(0).getMember().getId()).isEqualTo(anka.getId());
//    }

    @Test
    public void findBetweenDates() {
        Date start = new Date(250000000);
        Date end = new Date(450000000);
        //--- Когда репозиторий пуст
        assertThat(transactionRepository.findWithdrawBetweenDates(start,end).size()).isZero();

        Member anka = new Member("Анка");
        entityManager.persist(anka);
        entityManager.flush();

        Transaction transactionOne = new Transaction(new Date(50000),anka,-600L,"Empty");
        entityManager.persist(transactionOne);
        Transaction transactionTwo = new Transaction(new Date(350000000),anka,800L,"Empty");
        entityManager.persist(transactionTwo);
        entityManager.flush();

        //--- Когда репозиторий не пуст, но в нём нет отрицательных транзакций между нужными датами
        List<Transaction> notFound = transactionRepository.findWithdrawBetweenDates(start,end);
        assertThat(notFound.size()).isZero();

        Transaction transactionThree = new Transaction(new Date(360000000),anka,-1000L,"Empty");
        entityManager.persist(transactionThree);
        entityManager.flush();

        //--- Когда в репозитории присутствует нужный результат
        List<Transaction> found = transactionRepository.findWithdrawBetweenDates(start,end);
        Date current = found.get(0).getDate();
        assertThat(current).isBetween(start,end);
    }
}
