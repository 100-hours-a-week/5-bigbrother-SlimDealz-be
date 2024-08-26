package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.repository.User.UserRepository;
import bigbrother.slimdealz.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long findUserIdByKakao_Id(String kakao_Id) {
        return userRepository.findByKakao_Id(kakao_Id)
                .map(Member::getId)
                .orElse(null);
    }
}
