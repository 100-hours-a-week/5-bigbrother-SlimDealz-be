package bigbrother.slimdealz.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PythonCrawlerService {

//    public void runPythonCralwer() {
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("python3", "src/main/python/my_crawler.py");
//            processBuilder.redirectErrorStream(true);
//            Process process = processBuilder.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("크롤링 작업 종료, 종료 코드: " + exitCode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
//    public void scheduleCrawler() {
//        runPythonCralwer();
//    }
}
