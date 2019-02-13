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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberValidationTest {

    @Autowired
    private TestEntityManager entityManager;

    private Member member;
    private String memberName;

    @Before
    public void setUP() {
        member = new Member();
        memberName = "Any";
    }

    @Test
    public void validationId() {
        member.setName(memberName);
        Long id = (Long) entityManager.persistAndGetId(member);
        assertTrue(id > 0);
    }

    @Test(expected = PersistenceException.class)
    public void validationNotNullId() {
        member.setId(1L);
        member.setName(memberName);
        entityManager.persist(member);
    }

    @Test
    public void validationName() {
        member.setName(memberName);
        Long id = (Long) entityManager.persistAndGetId(member);
        assertTrue(id > 0);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationNullName() {
        entityManager.persist(member);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationBlankName() {
        member.setName("");
        entityManager.persist(member);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationNameMinLen() {
        member.setName("x");
        entityManager.persist(member);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validationNameMaxLen() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 31; i++) name.append("x");
        member.setName(name.toString());
        entityManager.persist(member);
    }

    @Test
    public void validationNameRegexp() {
        //TODO сделать генерацию набора запрещенных имён
        String[] names = {"aa?", "aa!", "aa,", "aa>", "aa  aa", " aa", "aa  ", "aa+", "-aa", "aa0", "a4a"};
        for (String name : names) {
            try {
                entityManager.persist(new Member(name));
                throw new RuntimeException("Member name validation test fail");
            } catch (ConstraintViolationException e) {
            }
        }

    }

}
