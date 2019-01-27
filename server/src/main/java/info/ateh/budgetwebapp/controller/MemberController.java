package info.ateh.budgetwebapp.controller;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MemberController {

    private final MemberService service;

    MemberController(MemberService service) {
        this.service=service;
    }

    /** GET: возвращает всех членов семьи*/
    @GetMapping("/members")
    private List<Member> all() {
        return service.getAllMembers();
    }

    /** GET: возвращает одного члена семьи соответствующим id */
    @GetMapping("/members/{id}")
    private Member one(@PathVariable Long id) {
        return service.getById(id);
    }

    /** GET: Возвращает результат проверки занято ли уже имя */
    @GetMapping("/members/isNameTaken/{name}")
    private boolean nameTaken(@PathVariable String name) {
        return service.isNameTaken(name);
    }

    /** POST: добавляет нового члена семьи */
    @PostMapping("/members")
    private Member addNew(@RequestBody Member newMember) {
        return service.addNewMember(newMember);
    }

    /** PUT: Заменяет члена семьи в репозитории или добавляет нового */
    @PutMapping("/members/{id}")
    private Member replace(@RequestBody Member newMember, @PathVariable Long id) {
        return service.replaceMember(newMember,id);
    }

    /** Удаляет члена семьи */
    @DeleteMapping("/members/{id}")
    private void delete(@PathVariable Long id) {
        service.deleteMember(id);
    }

}
