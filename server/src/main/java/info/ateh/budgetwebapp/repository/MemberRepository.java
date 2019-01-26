package info.ateh.budgetwebapp.repository;

import info.ateh.budgetwebapp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
public interface MemberRepository extends JpaRepository<Member, Long> {


    @Query("select m from Member m where UPPER(m.name) = UPPER(?1)")
    Member findByName(String name);
}
