//package bigbrother.slimdealz.controller.User;
//
//import bigbrother.slimdealz.dto.user.NotificationSendDto;
//import bigbrother.slimdealz.service.User.NotificationService;
//import io.jsonwebtoken.io.IOException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/fcm")
//public class NotificationController {
//    private final NotificationService fcmService;
//
//    public NotificationController(NotificationService fcmService) {
//        this.fcmService = fcmService;
//    }
//
//    @PostMapping("/send")
//    public ResponseEntity<ApiResponseWrapper<Object>> pushMessage(@RequestBody @Validated NotificationSendDto fcmSendDto) throws IOException {
//        log.debug("[+] 푸시 메시지를 전송합니다. ");
//        int result = fcmService.sendMessageTo(fcmSendDto);
//
//        ApiResponseWrapper<Object> arw = ApiResponseWrapper
//                .builder()
//                .result(result)
//                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
//                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
//                .build();
//        return new ResponseEntity<>(arw, HttpStatus.OK);
//    }
//}
