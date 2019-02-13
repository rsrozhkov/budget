package info.ateh.budgetwebapp.service;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.exception.MemberHasTransactionsException;
import info.ateh.budgetwebapp.exception.MemberNotFoundException;
import info.ateh.budgetwebapp.repository.MemberRepository;
import info.ateh.budgetwebapp.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class MemberServiceTest {

    @TestConfiguration
    static class MemberServiceTestContextConfiguration {
        @Bean
        public MemberService memberServiceTest() {
            return new MemberService();
        }
    }

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    private Member testMemberOne;
    private Member testMemberTwo;
    private List<Member> members;

    @Before
    public void setUp() {
        testMemberOne = new Member("TestMemberOneName");
        testMemberOne.setId(1L);
        testMemberTwo = new Member("TestMemberTwoName");
        testMemberTwo.setId(2L);

        members = new ArrayList<>();
        members.add(testMemberOne);
        members.add(testMemberTwo);

        Mockito.when(memberRepository.findAll()).thenReturn(members);
        Mockito.when(memberRepository.findById(testMemberOne.getId())).thenReturn(Optional.of(testMemberOne));
        Mockito.when(memberRepository.findByName(testMemberOne.getName())).thenReturn(testMemberOne);
        Mockito.when(memberRepository.save(testMemberOne)).thenReturn(testMemberOne);
        Mockito.when(memberRepository.save(testMemberTwo)).thenReturn(testMemberTwo);
        Mockito.when(memberRepository.save(null)).thenThrow(InvalidDataAccessApiUsageException.class);
        Mockito.when(transactionRepository.findByMemberId(testMemberTwo.getId())).thenThrow(MemberHasTransactionsException.class);
    }

    @Test
    public void getAllMembers() {
        List<Member> result = memberService.getAllMembers();
        assertThat(result.size()).isEqualTo(members.size());
        assertThat(result).contains(testMemberOne,testMemberTwo);
    }

    @Test
    public void getById() {
        Long id = testMemberOne.getId();
        Member found = memberService.getById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test(expected = MemberNotFoundException.class)
    public void getByNullId() {
        memberService.getById(null);
    }

    @Test(expected = MemberNotFoundException.class)
    public void getByNonexistentId() {
        memberService.getById(1000L);
    }

    @Test(expected = MemberNotFoundException.class)
    public void getByZeroId() {
        memberService.getById(0L);
    }

    @Test(expected = MemberNotFoundException.class)
    public void getByNegativeId() {
        memberService.getById(-100L);
    }

    @Test
    public void isNameTaken() {
        String name = testMemberOne.getName();
        assertTrue(memberService.isNameTaken(name));
        assertFalse(memberService.isNameTaken(name+"Free"));
    }

    @Test
    public void addNewMember() {
        Member result = memberService.addNewMember(testMemberOne);
        assertThat(result).isEqualTo(testMemberOne);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void addNewNullMember() {
        Member result = memberService.addNewMember(null);
    }

    @Test
    public void replaceMember() {
        Member result = memberService.replaceMember(testMemberTwo,testMemberOne.getId());
        assertThat(result).isEqualTo(testMemberOne);
    }

    @Test
    public void replaceNonexistentMember() {
        Member result = memberService.replaceMember(testMemberTwo,testMemberTwo.getId());
        assertThat(result).isEqualTo(testMemberTwo);
    }

    @Test
    public void deleteMember() {
        Long id = testMemberOne.getId();
        memberService.deleteMember(id);
        verify(transactionRepository).findByMemberId(id);
        verify(memberRepository).deleteById(id);
    }

    @Test(expected = MemberHasTransactionsException.class)
    public void deleteMemberHasTransactions() {
        Long id = testMemberTwo.getId();
        memberService.deleteMember(id);
    }

}
