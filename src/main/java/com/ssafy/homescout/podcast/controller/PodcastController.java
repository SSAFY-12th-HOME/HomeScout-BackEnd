package com.ssafy.homescout.podcast.controller;

import com.ssafy.homescout.annotation.Auth;
import com.ssafy.homescout.entity.Dongcode;
import com.ssafy.homescout.podcast.dto.PodcastScript;
import com.ssafy.homescout.podcast.dto.UserRole;
import com.ssafy.homescout.podcast.service.PodcastSchedulerService;
import com.ssafy.homescout.podcast.service.PodcastScriptGeneratorService;
import com.ssafy.homescout.podcast.service.PodcastService;
import com.ssafy.homescout.user.dto.UserRoleResponseDto;
import com.ssafy.homescout.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


@RestController
@RequiredArgsConstructor
@RequestMapping("/podcast")
public class PodcastController {

    private final PodcastService podcastService;
    private final UserService userService; // 사용자 정보를 조회하기 위한 서비스

    //스케줄러 테스트용
    private final PodcastSchedulerService podcastSchedulerService;
    private final PodcastScriptGeneratorService podcastScriptGeneratorService;


    //팟캐스트 생성 엔드포인트
    @GetMapping("/play")
    public ResponseEntity<PodcastScript> playPodcast(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @Auth Long userId) {

        // 사용자 역할만 조회
        UserRoleResponseDto userRole = userService.getUserRole(userId);
        PodcastScript podcastScript = podcastService.getPodcast(latitude, longitude, userRole.getRole());

        return ResponseEntity.ok(podcastScript);
    }



    //팟캐스트 생성 엔드포인트
    @GetMapping("/test")
    public ResponseEntity<?> playPodcastTest() {

        //podcastSchedulerService.generateAndUploadPodcasts();
        ArrayList<String[]> tests = new ArrayList<>();
        tests.add(new String[] {"Host", "저는 호스트입니다!"});
        tests.add(new String[] {"Guest", "저는 게스트입니다!!"});

        ArrayList<Byte> bytes = podcastScriptGeneratorService.generateVoice(tests);

//        byte[] result = new byte[bytes.size()];
//
//        for (int i = 0; i < bytes.size(); i++) {
//            result[i] = bytes.get(i);
//        } //모든 바이트가 합쳐진 결과 생성 -> result
//
//        try {
//            FileOutputStream fos = new FileOutputStream("test123.mp3"); //test123 라는 이름으로 저장 후 mp3 방식으로 읽겠다.
//            fos.write(result); //test123.mp3를 저장함
//            fos.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        Dongcode ssg = new Dongcode();
//        ssg.setDongCd("test");
//        podcastSchedulerService.saveToS3(ssg, UserRole.NORMAL, result);

        return ResponseEntity.ok("ok");
    }

}
