package moonz.core.member;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component  // 자동 빈 등록
public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> store = new HashMap<>();  // 실무에서는 동시성 문제때문에 cuncurrent hashmap을 써야함.
    // 주의 : Map을 static으로 생성하지 않으면 다른 곳에서 같은 메로리를 공유하지 못함

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
