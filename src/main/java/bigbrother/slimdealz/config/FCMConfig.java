//package bigbrother.slimdealz.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FCMConfig {
//
//    @Bean
//    public FirebaseApp firebaseApp() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase/FirebaseServiceKey.json");
//
//        // 서비스 계정 옵션 초기화
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        // FirebaseApp 초기화
//        if(FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options);
//        }
//
//        return FirebaseApp.getInstance();
//    }
//}
