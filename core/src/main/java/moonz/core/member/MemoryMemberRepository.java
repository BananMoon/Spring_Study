package moonz.core.member;

import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository{
    private Map<Long, Member> store = new HashMap<>();  // 실무에서는 동시성 문제때문에 cuncurrent hashmap을 써야함.

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
