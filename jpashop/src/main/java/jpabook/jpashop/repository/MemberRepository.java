package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // ComponentScan 대상
public class MemberRepository {
    // Entity Manager 필요
    @PersistenceContext // Spring이 관리하는 Entity Manager를 주입받을 수 있음
    EntityManager em;

    // EntityManager Factory를 직접 주입받고 싶다면, 아래 코드.
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    public Long save(Member member) {
        em.persist(member); // 영속성 컨텍스트에 member 객체를 넣고, 커밋시점에 db에 반영됨.
        // command와 query분리해라. side effect를 일으킬 수 있는 command성이므로 리턴값으로 객체X
        return member.getId();
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    public List<Member> findAll() {
        // JPQL : Entity 대상으로 쿼리 (SQL은 Table 대상으로 쿼리)
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName (String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}
