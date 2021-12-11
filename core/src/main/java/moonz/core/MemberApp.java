package moonz.core;

import moonz.core.member.Grade;
import moonz.core.member.Member;
import moonz.core.member.MemberService;

public class MemberApp {
    public static void main(String[] args) {
        //   수정
        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();

        Member member = new Member(1L, "memberA", Grade.VIP);   // Ctrl+ alt + V : 변수 생성
        memberService.join(member);
        Member findMember = memberService.findMember(1L);
        System.out.println("new Member= " + member.getName());
        System.out.println("find Member= " + findMember.getName());
    }
}
