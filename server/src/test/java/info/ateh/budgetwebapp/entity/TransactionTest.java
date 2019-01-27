package info.ateh.budgetwebapp.entity;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.xml.crypto.Data;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class TransactionTest {
    @MockBean private Member member;
    @MockBean private Long amount;

    //TODO Эти тесты перенести в TransactionServiceTest
/*
    @Test
    public void getComment() {
        String inputValueEmpty = "";
        String expectedValueEmpty = "";
        String actualValueEmpty = new Transaction(member,amount,inputValueEmpty).getComment();
        assertEquals(expectedValueEmpty,actualValueEmpty);

        String inputValueNormal = "Njnk wevkjn ewvk = + dsr./' doi";
        String expectedValueNormal = "Njnk wevkjn ewvk = + dsr./' doi";
        String actualValueNormal = new Transaction(member,amount,inputValueNormal).getComment();
        assertEquals(expectedValueNormal,actualValueNormal);

        String inputValueSpeces = "  d  Njnk wevkjn ewvk = + dsr./' doi   ";
        String expectedValueSpaces = "d Njnk wevkjn ewvk = + dsr./' doi";
        String actualValueSpeces = new Transaction(member,amount,inputValueSpeces).getComment();
        assertEquals(expectedValueSpaces,actualValueSpeces);
    }

    @Test
    public void dateFieldCreationTest() throws InterruptedException {
        Date start = new Date();
        Thread.sleep(100L);
        Date actualValue = new Transaction(member,amount,"Пусто").getDate();
        Thread.sleep(100L);
        Date end = new Date();
        assertThat(actualValue).isBetween(start,end);
    }
*/
}