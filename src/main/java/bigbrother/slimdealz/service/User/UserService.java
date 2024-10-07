package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.repository.User.UserRepository;
import bigbrother.slimdealz.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long findUserIdByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .map(Member::getId)
                .orElse(null);
    }
}
