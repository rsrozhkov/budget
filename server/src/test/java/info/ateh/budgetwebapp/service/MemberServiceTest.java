package info.ateh.budgetwebapp.service;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.exception.MemberNotFoundException;
import info.ateh.budgetwebapp.repository.MemberRepository;
import info.ateh.budgetwebapp.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    MemberService service;

    @Test
    public void getAllMembers() {
        service.getAllMembers();
        verify(memberRepository).findAll();
    }

    @Test(expected = MemberNotFoundException.class)
    public void getById() {
        Long id = 1L;
        service.getById(id);
        verify(memberRepository).findById(id);
    }

    @Test
    public void isNameTaken() {
        String name = "Анка";
        service.isNameTaken(name);
        verify(memberRepository).findByName(name);
    }

    @Test
    public void addNewMember() {
        Member member  = new Member("Анка");
        service.addNewMember(member);
        verify(memberRepository).save(member);
    }

    @Test
    public void replaceMember() {
        Member member  = new Member("Анка");
        service.replaceMember(member,member.getId());
        verify(memberRepository).save(member);
    }

    @Test//(expected = MemberHasTransactionsException.class)
    public void deleteMember() {
        Long id = 1L;
        service.deleteMember(id);
        verify(transactionRepository).findByMemberId(id);
        verify(memberRepository).deleteById(id);
    }
}