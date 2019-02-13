package info.ateh.budgetwebapp.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class MemberTest {

    @Test
    public void constructors() {
        String name = "Any";
        Member memberDefault = new Member();
        Member memberWithName = new Member(name);
        assertNotNull(memberDefault);
        assertNotNull(memberWithName);
        assertNull(memberDefault.getId());
        assertNull(memberWithName.getId());
        assertNull(memberDefault.getName());
        assertNotNull(memberWithName.getName());
        assertEquals(memberWithName.getName(), name);
    }

    @Test
    public void getId() {
        Long id = 100L;
        Member member = new Member();
        assertNull(member.getId());
        member.setId(id);
        assertEquals(member.getId(), id);
    }

    @Test
    public void getName() {
        String name = "Any";
        Member member = new Member(name);
        assertEquals(member.getName(), name);
    }

    @Test
    public void setId() {
        Member member = new Member();
        member.setId(-1L);
        assertEquals(member.getId().longValue(),-1);
        member.setId(1L);
        assertEquals(member.getId().longValue(),1);
        member.setId(0L);
        assertEquals(member.getId().longValue(),0);
        member.setId(null);
        assertNull(member.getId());
    }

    @Test
    public void setName() {
        String nameOne = "AnyFirst";
        String nameTwo = "AnySecond";
        Member member = new Member();
        member.setName(nameOne);
        assertEquals(member.getName(), nameOne);
        member.setName(nameTwo);
        assertEquals(member.getName(), nameTwo);
        member.setName("");
        assertTrue(member.getName().isEmpty());
        member.setName(null);
        assertNull(member.getName());
    }

}
