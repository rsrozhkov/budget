package info.ateh.budgetwebapp.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class TransactionTest {

    @MockBean
    private Member member;

    @Test
    public void constructors() throws InterruptedException {
        Transaction transactionDefault = new Transaction();
        assertNotNull(transactionDefault);
        assertNull(transactionDefault.getId());
        assertNull(transactionDefault.getDate());
        assertNull(transactionDefault.getMember());
        assertNull(transactionDefault.getAmount());
        assertNull(transactionDefault.getComment());
        Long amount = 100L;
        String comment = "Any";
        Date before = new Date();
        Thread.sleep(1);
        Transaction transaction = new Transaction(member, amount,comment);
        Thread.sleep(1);
        Date after = new Date();
        assertNotNull(transaction);
        assertNull(transaction.getId());
        assertTrue(transaction.getDate().after(before) && transaction.getDate().before(after));
        assertEquals(transaction.getMember(), member);
        assertEquals(transaction.getAmount(), amount);
        assertEquals(transaction.getComment(), comment);
    }

    @Test
    public void getId() {
        Long id = 100L;
        Transaction transaction = new Transaction();
        assertNull(transaction.getId());
        transaction.setId(id);
        assertEquals(transaction.getId(), id);
    }

    @Test
    public void getDate() {
        Date date = new Date();
        Transaction transaction = new Transaction();
        assertNull(transaction.getDate());
        transaction.setDate(date);
        assertEquals(transaction.getDate(), date);
    }

    @Test
    public void getMember() {
        Transaction transaction = new Transaction();
        assertNull(transaction.getMember());
        transaction.setMember(member);
        assertEquals(transaction.getMember(), member);
    }

    @Test
    public void getAmount() {
        Long amount = 100L;
        Transaction transaction = new Transaction();
        assertNull(transaction.getAmount());
        transaction.setAmount(amount);
        assertEquals(transaction.getAmount(), amount);
    }

    @Test
    public void getComment() {
        String comment = "Any";
        Transaction transaction = new Transaction();
        assertNull(transaction.getComment());
        transaction.setComment(comment);
        assertEquals(transaction.getComment(), comment);
    }

    @Test
    public void setId() {
        Transaction transaction = new Transaction();
        transaction.setId(-1L);
        assertEquals(transaction.getId().longValue(),-1);
        transaction.setId(1L);
        assertEquals(transaction.getId().longValue(),1);
        transaction.setId(0L);
        assertEquals(transaction.getId().longValue(),0);
        transaction.setId(null);
        assertNull(transaction.getId());
    }

    @Test
    public void setDate() throws InterruptedException {
        Date before = new Date();
        Thread.sleep(1);
        Date current = new Date();
        Thread.sleep(1);
        Date after = new Date();
        Transaction transaction = new Transaction();
        transaction.setDate(current);
        assertEquals(transaction.getDate(), current);
        assertTrue(transaction.getDate().after(before));
        assertTrue(transaction.getDate().before(after));
        transaction.setDate(after);
        assertEquals(transaction.getDate(), after);
        transaction.setDate(null);
        assertNull(transaction.getDate());
    }

    @Test
    public void setMember() {
        Member secondMember = new Member();
        Transaction transaction = new Transaction();
        transaction.setMember(member);
        assertEquals(transaction.getMember(), member);
        transaction.setMember(secondMember);
        assertEquals(transaction.getMember(), secondMember);
        transaction.setDate(null);
        assertNull(transaction.getDate());
    }

    @Test
    public void setAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(-1L);
        assertEquals(transaction.getAmount().longValue(),-1);
        transaction.setAmount(1L);
        assertEquals(transaction.getAmount().longValue(),1);
        transaction.setAmount(0L);
        assertEquals(transaction.getAmount().longValue(),0);
        transaction.setAmount(null);
        assertNull(transaction.getAmount());
    }

    @Test
    public void setComment() {
        String commentOne = "AnyFirst";
        String commentTwo = "AnySecond";
        Transaction transaction = new Transaction();
        transaction.setComment(commentOne);
        assertEquals(transaction.getComment(), commentOne);
        transaction.setComment(commentTwo);
        assertEquals(transaction.getComment(), commentTwo);
        transaction.setComment("");
        assertTrue(transaction.getComment().isEmpty());
        transaction.setComment(null);
        assertNull(transaction.getComment());
    }

}
