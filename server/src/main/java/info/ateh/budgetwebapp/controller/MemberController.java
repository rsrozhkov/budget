package info.ateh.budgetwebapp.controller;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.exception.*;
import info.ateh.budgetwebapp.repository.MemberRepository;
import info.ateh.budgetwebapp.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.util.List;

import static info.ateh.budgetwebapp.utils.Constants.*;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MemberController {

    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    MemberController(MemberRepository memberRepository,TransactionRepository transactionRepository) {
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
    }

    /** GET: возвращает всех членов семьи найденых репозитории*/
    @GetMapping("/members")
    private List<Member> all() {
        return memberRepository.findAll();
    }

    /** GET: возвращает одного члена семьи соответствующим id,
     * либо кидает исключение */
    @GetMapping("/members/{id}")
    private Member one(@PathVariable @Positive @NotNull Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    /** GET: проверяет занято ли имя члена семьи и возвращает результат проверки */
    @GetMapping("/members/isNameTaken/{name}")
    private boolean isNameTaken(@PathVariable @NotNull @NotBlank
                        @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN)
                        @Pattern(regexp = NAME_REGEXP) String name) {
        return memberRepository.findByName(name) != null;
    }

    /** POST: добавляет нового члена семьи в репозиторий  */
    @PostMapping("/members")
    private Member newMember(@RequestBody @NotNull Member newMember) {
        return memberRepository.save(newMember);
    }

    /** PUT: Заменяет члена семьи в репозитории или добавляет нового */
    @PutMapping("/members/{id}")
    private Member replaceMember(@RequestBody @NotNull Member newMember,
                         @PathVariable @Positive @NotNull Long id) {
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

    @DeleteMapping("/members/{id}")
    private void deleteMember(@PathVariable @Positive @NotNull Long id) {
        if (transactionRepository.findByMemberId(id).size() > 0)
            throw new MemberHasTransactionsException(id);
        else memberRepository.deleteById(id);
    }

}
