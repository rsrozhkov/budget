package info.ateh.budgetwebapp.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionValidationTest {

    @Autowired
    private TestEntityManager entityManager;

    private Member member;
    private Transaction transaction;

    @Before
    public void setUp() {
        member = new Member("Any");
        entityManager.persist(member);
        transaction = new Transaction(member,100L,"Any");
    }

    @Test
    public void validationId() {
        Long id = (Long) entityManager.persistAndGetId(transaction);
        assertTrue(id >0);
    }

    @Test(expected = PersistenceException.class)
    public void validationNotNullId() {
        transaction.setId(1L);
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationNullDate() {
        transaction.setDate(null);
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationFutureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,1);
        transaction.setDate(calendar.getTime());
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationNullMember() {
        transaction.setMember(null);
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationNullComment() {
        transaction.setComment(null);
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationBlankComment() {
        transaction.setComment("");
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationCommentMinLen() {
        transaction.setComment("ab");
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationCommentMaxLen() {
        StringBuilder comment = new StringBuilder();
        for (int i = 0; i < 151; i++) comment.append("x");
        transaction.setComment(comment.toString());
        entityManager.persist(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationCommentRegexp() {
        transaction.setComment("abc\n");
        entityManager.persist(transaction);
    }

}
