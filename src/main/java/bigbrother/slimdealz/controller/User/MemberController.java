package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.dto.MemberDTO;
import bigbrother.slimdealz.service.User.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")

    public Map<String, String> signUp(@RequestBody MemberDTO memberDTO) {
        log.info("--------------------------- MemberController ---------------------------");
        log.info("memberDTO = {}", memberDTO);
        Map<String, String> response = new HashMap<>();

        // 이메일 대신 socialId로 회원을 검색
        Optional<Member> bySocialId = memberService.findBySocialId(memberDTO.getSocialId());
        if (bySocialId.isPresent()) {
            response.put("error", "이미 존재하는 회원입니다");
        } else {
            memberService.saveMember(memberDTO);
            response.put("success", "성공적으로 처리하였습니다");
        }
        return response;
    }
}
