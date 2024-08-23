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

<<<<<<< Updated upstream
<<<<<<< Updated upstream
    @PutMapping("/api/v1/users/{socialId}/profile")
    public Map<String, String> updateMemberProfile(
            @PathVariable String socialId,
=======
    @PutMapping("/api/v1/users/{kakao_Id}/profile")
    public Map<String, String> updateMemberProfile(
            @PathVariable String kakao_Id,
>>>>>>> Stashed changes
=======
    @PutMapping("/api/v1/users/{kakao_Id}/profile")
    public Map<String, String> updateMemberProfile(
            @PathVariable String kakao_Id,
>>>>>>> Stashed changes
            @RequestBody MemberDTO memberDTO) {

        Map<String, String> response = new HashMap<>();

        try {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            memberService.updateMemberProfile(socialId, memberDTO);
=======
            memberService.updateMemberProfile(kakao_Id, memberDTO);
>>>>>>> Stashed changes
=======
            memberService.updateMemberProfile(kakao_Id, memberDTO);
>>>>>>> Stashed changes
            response.put("success", "회원 정보가 성공적으로 수정되었습니다");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
    @GetMapping("/api/v1/users/{socialId}/profile")
    public MemberDTO getMemberProfile(@PathVariable String socialId) {
        Optional<Member> optionalMember = memberService.findBySocialId(socialId);
=======
    @GetMapping("/api/v1/users/{kakao_Id}/profile")
    public MemberDTO getMemberProfile(@PathVariable String kakao_Id) {
        Optional<Member> optionalMember = memberService.findByKakaoId(kakao_Id);
>>>>>>> Stashed changes
=======
    @GetMapping("/api/v1/users/{kakao_Id}/profile")
    public MemberDTO getMemberProfile(@PathVariable String kakao_Id) {
        Optional<Member> optionalMember = memberService.findByKakaoId(kakao_Id);
>>>>>>> Stashed changes
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setName(member.getName());
            memberDTO.setNickname(member.getNickname());
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            memberDTO.setSocialId(member.getSocialId());
            memberDTO.setProfileImage(member.getProfileImage());
            memberDTO.setCardInfo(member.getCardInfo());
            memberDTO.setReceiveNotification(member.isReceiveNotification());
            return memberDTO;
        } else {
            throw new RuntimeException("User not found with socialId: " + socialId);
=======
=======
>>>>>>> Stashed changes
            memberDTO.setKakao_Id(member.getKakao_Id());
            memberDTO.setProfileImage(member.getProfileImage());
            memberDTO.setCard(member.getCard());
            memberDTO.setNotification_agree(member.isNotification_agree());
            return memberDTO;
        } else {
            throw new RuntimeException("User not found with kakao_Id: " + kakao_Id);
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
        }
    }
}
