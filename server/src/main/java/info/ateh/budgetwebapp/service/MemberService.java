package info.ateh.budgetwebapp.service;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.exception.MemberHasTransactionsException;
import info.ateh.budgetwebapp.exception.MemberNotFoundException;
import info.ateh.budgetwebapp.repository.MemberRepository;
import info.ateh.budgetwebapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.*;
import java.util.List;

import static info.ateh.budgetwebapp.utils.Constants.*;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    /** Возвращает всех членов семьи найденых репозитории*/
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    /** Возвращает одного члена семьи с соответствующим id,
     * если не найден,то кидает исключение */
    public Member getById(@Positive @NotNull Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    /** Проверяет есть ли в репозитории переданное имя члена семьи и возвращает результат проверки */
    public boolean isNameTaken(@NotNull @NotBlank @Pattern(regexp = NAME_REGEXP)
                                @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN)
                                       String name) {
        return memberRepository.findByName(name) != null;
    }

    /** Добавляет нового члена семьи в репозиторий  */
    public Member addNewMember(@NotNull Member newMember) {
        return memberRepository.save(newMember);
    }

    /** Заменяет члена семьи в репозитории, или добавляет нового */
    public Member replaceMember(@NotNull Member newMember, @Positive @NotNull Long id) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setName(newMember.getName());
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    newMember.setId(id);
                    newMember.setName(newMember.getName());
                    return memberRepository.save(newMember);
                });
    }

    /** Удаляет члена семьи в случае отсутствия у него транзакций */
    public void deleteMember(@Positive @NotNull Long id) {
        if (transactionRepository.findByMemberId(id).size() > 0)
            throw new MemberHasTransactionsException(id);
        memberRepository.deleteById(id);
    }
}
