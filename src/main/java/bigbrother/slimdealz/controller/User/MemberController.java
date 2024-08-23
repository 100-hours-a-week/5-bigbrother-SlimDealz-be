package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.dto.MemberDTO;
import bigbrother.slimdealz.service.User.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/users/kakaologin")
    public Map<String, String> signUp(@RequestBody MemberDTO memberDTO) {
        log.info("--------------------------- MemberController ---------------------------");
        log.info("memberDTO = {}", memberDTO);
        Map<String, String> response = new HashMap<>();

        // 이메일 대신 kakaoId 회원을 검색
        Optional<Member> byKakaoId = memberService.findByKakaoId(memberDTO.getKakao_Id());
        if (byKakaoId.isPresent()) {
            response.put("error", "이미 존재하는 회원입니다");
        } else {
            memberService.saveMember(memberDTO);
            response.put("success", "성공적으로 처리하였습니다");
        }
        return response;
    }

    @PutMapping("/api/v1/users/{kakao_Id}/profile")
    public Map<String, String> updateMemberProfile(
            @PathVariable String kakao_Id,
            @RequestBody MemberDTO memberDTO) {

        Map<String, String> response = new HashMap<>();

        try {
            memberService.updateMemberProfile(kakao_Id, memberDTO);
            response.put("success", "회원 정보가 성공적으로 수정되었습니다");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/api/v1/users/{kakao_Id}/profile")
    public MemberDTO getMemberProfile(@PathVariable String kakao_Id) {
        Optional<Member> optionalMember = memberService.findByKakaoId(kakao_Id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setName(member.getName());
            memberDTO.setNickname(member.getNickname());
            memberDTO.setKakao_Id(member.getKakao_Id());
            memberDTO.setProfileImage(member.getProfileImage());
            memberDTO.setCard(member.getCard());
            memberDTO.setNotification_agree(member.isNotification_agree());
            return memberDTO;
        } else {
            throw new RuntimeException("User not found with kakao_Id: " + kakao_Id);
        }
    }
}
