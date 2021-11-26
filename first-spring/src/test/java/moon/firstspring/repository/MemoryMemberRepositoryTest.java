package moon.firstspring.repository;

import moon.firstspring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

public class MemoryMemberRepositoryTest  {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }
    @Test
    public void save() {
        Member member = new Member();
        member.setName("Moon");
        repository.save(member);
        Member result = repository.findById(member.getId()).get();  // get(): 좋은 방법은 아님.
        // 똑같은지 체크
//        System.out.println("result = " + (result == member));  : 이렇게도 가능하긴 하지만
//        Assertions.assertEquals(result, member);    // (기대, 실제)
        Assertions.assertThat(member).isEqualTo(result);    // 눈에 잘보이는 것은 이것.
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        Assertions.assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}
