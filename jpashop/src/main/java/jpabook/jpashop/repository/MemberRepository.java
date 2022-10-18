package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // ComponentScan 대상
public class MemberRepository {
    // Entity Manager 필요
    @PersistenceContext // Spring이 관리하는 Entity Manager를 주입받을 수 있음
    private final EntityManager em;

    // Spring Data JPA에서는 @PersistenceContext 를 그냥 autowired로 받을 수 있도록 해서
    // private final EneityManager em;
    public MemberRepository(EntityManager em) { //혹은 @RequiredArgsConstructor
        this.em = em;
    }

    // EntityManager Factory를 직접 주입받고 싶다면, 아래 코드.
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    public Long save(Member member) {
        em.persist(member); // 영속성 컨텍스트에 member 객체를 넣는다. 이때 key는 객체의 pk고, 그때 자동으로 객체의 id를 생성해서 넣어준다.
        // 커밋시점에 db에 반영되는 것임..
        return member.getId();  // command와 query분리해라. side effect를 일으킬 수 있는 command성이므로 리턴값으로 객체X
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    public List<Member> findAll() {
        // JPQL : Entity 대상으로 쿼리 (SQL은 Table 대상으로 쿼리)
        // 기본 생성자(default Constructor)가 있어야 쿼리된 데이터가 Member 객체로 생성됨.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName (String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}
