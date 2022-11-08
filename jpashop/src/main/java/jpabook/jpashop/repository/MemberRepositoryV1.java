package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 해당 클래스와 같이 EntityManager를 이용해서 메서드를 호출하여 영속성 관리 진행.
 * 이를 JpaRepository를 상속받는 인터페이스로 변환하면, Spring Data Jpa가 아래 메서드들을 기본으로 제공하여
 * MemberRepositoryV2처럼 단순해질 수 있다.
 */
@Repository // ComponentScan 대상
public class MemberRepositoryV1 {
    /* Entity Manager 필요 */
    /* Spring Data JPA에서는 @PersistenceContext 없이도 그냥 autowired로 받을 수 있도록 한다. */
//    @PersistenceContext // Spring이 관리하는 Entity Manager를 주입받을 수 있음
    private final EntityManager em;
    public MemberRepositoryV1(EntityManager em) {  /* @RequiredArgsConstructor로 대체 가능 */
        this.em = em;
    }

    /* EntityManager Factory를 직접 주입받고 싶다면? */
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    /* 커밋시점에 db에 반영되는 것 */
    public Long save(Member member) {
        em.persist(member);     /* 영속성 컨텍스트에 member 객체를 넣는다. 이때 key는 객체의 pk. -> 자동으로 객체의 id를 생성해서 넣어준다. */
        return member.getId();  /* command와 query분리하자. side effect를 일으킬 수 있는 command성이므로 리턴값으로 객체를 반환하지 않고 필요하다면 다시 findById() 진행한다. */
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
