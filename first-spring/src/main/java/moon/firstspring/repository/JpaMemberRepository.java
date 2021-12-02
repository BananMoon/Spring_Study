package moon.firstspring.repository;

import moon.firstspring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); // insert쿼리 생성 후 집어 놓고, id도 set해준다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);   //entity 클래스를 넣어줘야함
        return Optional.ofNullable(member); // 값이 없을 수 있으니 Optional.ofNullable()
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();   // 연속적인 stream 중 가장 먼저 발견되는 거 한개 반환
    }

    @Override
    public List<Member> findAll() {
        //inline
        return em.createQuery("select m from Member m where m.name = :name" , Member.class)    // list로 만들어줘야함.
                        .getResultList();
    }
}
