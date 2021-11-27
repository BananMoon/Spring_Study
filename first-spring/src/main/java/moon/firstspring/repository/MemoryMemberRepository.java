package moon.firstspring.repository;

import moon.firstspring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
//@Repository
public class MemoryMemberRepository  implements MemberRepository{
    private static Map<Long, Member> store = new HashMap<>();   // 실무에서는 공유되는 값은 ConcurrentHashMap 사용!
    private static long sequence = 0L;  // 실무에서는 동시성문제로, AtomicLong 사용

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }
    // Stream 을 이용하여 람다함수형식으로 간결하고 깔끔하게 요소들의 처리가 가능 (자바 8~)
    // 배열 원소 가공에는 map, filter, sorted 등이 있음.
    //  list.stream().map(s -> s.toUpperCase()); : 요소들을 특정 조건에 해당하는 값으로 변형
    // list.stream().filter(t -> t.getName().equals(s));  : 요소들을 특정 조건에 따라 걸러내는 작업
    // list.stream().sorted() : 요소들을 정렬

    @Override
    public List<Member> findAll() {
        // java 실무에서는 loop으로 돌리기 편해서 List를 많이 쓰므로, ArrayList로 변환 후 return
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
