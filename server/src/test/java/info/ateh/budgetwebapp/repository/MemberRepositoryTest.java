package info.ateh.budgetwebapp.repository;

import info.ateh.budgetwebapp.entity.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void findByName() {
        //--- Когда в репозитории пусто
        assertThat(memberRepository.findByName("Анка")).isNull();

        Member anka = new Member("Анка");
        entityManager.persist(anka);
        entityManager.flush();

        //--- Когда репозиторий не пуст но в нем отсутствует нужное имя
        assertThat(memberRepository.findByName("Петька")).isNull();

        //--- Когда в репозитории присутствует нужное имя
        assertThat(memberRepository.findByName(anka.getName()).getName())
                .isEqualTo(anka.getName());
    }
}
