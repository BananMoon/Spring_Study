package jpabook.jpashop.api;

import jpabook.jpashop.common.Result;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.CreateMemberDto;
import jpabook.jpashop.dto.ListMemberDto;
import jpabook.jpashop.dto.UpdateMemberDto;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v2/members")
    public Result<List<ListMemberDto>> members() {
        List<ListMemberDto> collect = memberService.findMembers()
                .stream()
                .map(m -> new ListMemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result<>(collect);
    }
    @PostMapping("/api/v2/member")
    public CreateMemberDto.Response saveMember(@RequestBody CreateMemberDto.Request request) {
        Member member = Member.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();
        long id = memberService.join(member);
        CreateMemberDto.Response response = new CreateMemberDto.Response();
        response.setId(id);
        return response;
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberDto.Response updateMember (@PathVariable Long id,
                                                  @RequestBody @Valid UpdateMemberDto.Request request) {
        /* 커맨드와 쿼리 분리 - PK로 조회 1개 하는 것은 큰 부담이 아니므로 */
        memberService.update(id, request.getName());
        Member updatedMember = memberService.findOne(id);

        return new UpdateMemberDto.Response(updatedMember.getId(), updatedMember.getName());
    }
}
