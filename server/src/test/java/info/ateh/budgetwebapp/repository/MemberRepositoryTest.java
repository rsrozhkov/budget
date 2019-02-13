package info.ateh.budgetwebapp.repository;

import info.ateh.budgetwebapp.entity.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @Before
    public void setUp() {
        testMember = new Member("TestMemberName");
    }

    @Test
    public void findAll() {
        List<Member> result = memberRepository.findAll();
        System.out.println(result);
        assertThat(result).isNotNull();
        assertThat(result).doesNotContainNull();
        assertThat(result.get(0).getId() > 0).isTrue();
    }

    @Test
    public void findById() {
        Long id = (Long) entityManager.persistAndGetId(testMember);
        Optional<Member> result = memberRepository.findById(id);
        assertThat(testMember).isEqualTo(result.orElse(null));
        entityManager.remove(testMember);
        assertThat(memberRepository.findById(id)).isEmpty();
        assertThat(memberRepository.findById(0L)).isEmpty();
        assertThat(memberRepository.findById(-1L)).isEmpty();
    }

    @Test
    public void findByName() {
        assertThat(memberRepository.findByName(testMember.getName())).isNull();
        entityManager.persist(testMember);
        assertThat(memberRepository.findByName(testMember.getName()+"X")).isNull();
        Member result = memberRepository.findByName(testMember.getName());
        assertThat(result).isEqualTo(testMember);
    }

    @Test
    public void save() {
        assertThat(memberRepository.findByName(testMember.getName())).isNull();
        memberRepository.save(testMember);
        memberRepository.save(testMember);
        assertThat(memberRepository.findByName(testMember.getName())).isEqualTo(testMember);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveNull() {
        memberRepository.save(null);
    }

    @Test(expected = DataIntegrityViolationException.class)
        public void saveSameName() {
        Member member = new Member(testMember.getName());
        memberRepository.save(testMember);
        memberRepository.save(member);
    }

    @Test
    public void deleteById() {
        Long id = (Long) entityManager.persistAndGetId(testMember);
        assertThat(memberRepository.findById(id)).isNotEmpty();
        memberRepository.deleteById(id);
        assertThat(memberRepository.findById(id)).isEmpty();
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteByNonexistentId() {
        Long id = (Long) entityManager.persistAndGetId(testMember);
        entityManager.remove(testMember);
        memberRepository.deleteById(id);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void deleteByNullId() {
        memberRepository.deleteById(null);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteByZeroId() {
        memberRepository.deleteById(0L);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteByNegativeId() {
        memberRepository.deleteById(-1L);
    }
}
