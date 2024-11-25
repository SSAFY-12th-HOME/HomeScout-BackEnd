package com.ssafy.homescout.podcast.service;

import com.ssafy.homescout.entity.Dongcode;
import com.ssafy.homescout.podcast.dto.NewsArticle;
import com.ssafy.homescout.podcast.dto.UserRole;
import com.ssafy.homescout.podcast.mapper.PodcastMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PodcastSchedulerService {

    private final PodcastMapper podcastMapper;

    private final NewsCrawlerService newsCrawlerService;
    private final PodcastScriptGeneratorService scriptGeneratorService;
    private final PodcastS3ManagerService s3ManagerService;


    @Scheduled(cron = "00 00 06 * * *")
    public void generateAndUploadPodcasts() {
        log.info("팟캐스트 생성 스케줄러 시작.");

        //dongCd, sido, sgg 데이터 가져옴 -> dong_nm가 없는 데이터만 select 했기 때문에 중복 없는 데이터 조회 가능
        List<Dongcode> dongcodes = podcastMapper.selectAllDongCd();

        for (Dongcode sgg : dongcodes) {

            //토큰 부족해서 우선 이렇게 설정
            if(!sgg.getSidoNm().equals("서울특별시")){
                break;
            }


            for (UserRole role : UserRole.values()) {
                try {
                    log.info("팟캐스트 생성: 시군구={}, 역할={}", sgg.getSggNm(), role.getRoleName());

                    // 1. 뉴스 데이터 수집
                    List<NewsArticle> articles = newsCrawlerService.crawlNewsData( sgg.getSggNm());
                    log.info("뉴스 데이터 수집 완료: 시군구={}, 기사 수={}",  sgg.getSggNm(), articles.size());

                    // 2. 팟캐스트 대본 생성
                    ArrayList<String[]> podcastScriptText = scriptGeneratorService.generatePodcastScript(articles, role.getRoleName());
                    log.info("팟캐스트 대본 생성 완료: 시군구={}, 역할={}",  sgg.getSggNm(), role.getRoleName());

                    // TODO TTS 처리 : 대사들을 모두 합친 바이트 파일
                    ArrayList<Byte> result = scriptGeneratorService.generateVoice(podcastScriptText);

                    byte[] resultCombine = new byte[result.size()];

                    for (int i = 0; i < result.size(); i++) {
                        resultCombine[i] = result.get(i);
                    } //모든 바이트가 합쳐진 결과 생성 == resultCombine


                    //S3에 지역코드_ROLE.mp3 로 저장
                    saveToS3(sgg, role, resultCombine);

                } catch (Exception e) {
                    log.error("팟캐스트 생성 실패: 시군구={}, 역할={}, 오류={}",  sgg.getSggNm(), role.getRoleName(), e.getMessage());
                    // 필요에 따라 예외를 처리하거나 알림을 보낼 수 있습니다.
                }
            }
            break;
        }

        log.info("팟캐스트 생성 스케줄러 완료.");
        System.out.println();
    }

    public void saveToS3(Dongcode sgg, UserRole role, byte[] ttsResult) {
        // S3 객체 키 생성(이름 생성)
        String fileName = s3ManagerService.generateObjectKey( sgg.getDongCd(), role.getRoleName());
        //fileName : "11100000000_NORMAL.mp3"


        // TTS mp3 업로드
        String podcastUrl = s3ManagerService.uploadPodcast(ttsResult, fileName); // mp3 데이터, 경로

        // 현재 Clova TTS 미사용, S3에 이미 mp3 파일이 있다고 가정
        //String podcastUrl = s3ManagerService.getPodcastUrl(fileName);
        log.info("팟캐스트 S3 URL 획득 완료: {}", podcastUrl);
    }

}
