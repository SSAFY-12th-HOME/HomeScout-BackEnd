package com.ssafy.homescout.podcast.service;

import com.ssafy.homescout.map.service.MapService;
import com.ssafy.homescout.podcast.dto.Participant;
import com.ssafy.homescout.podcast.dto.PodcastScript;
import com.ssafy.homescout.podcast.dto.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PodcastService {

    private final PodcastS3ManagerService s3ManagerService;
    private final NewsCrawlerService newsCrawlerService; // 시군구 이름 가져오기 용
    private final MapService mapService;

     //사용자 요청 시 S3에서 mp3 URL을 가져와 PodcastScript 객체를 생성하여 반환한다.
    public PodcastScript getPodcast(Double latitude, Double longitude, String userRole) {

        // 1. 사용자의 시군구 이름 가져오기
        String districtName = mapService.getRegionName(latitude, longitude);
        System.out.println("현재 요청 된 시군구: "+districtName);

        String districtCode = mapService.getRegionCode(latitude, longitude);
        System.out.println("현재 요청 된 시군구 코드: "+districtCode);

        if (districtCode == null) {
            throw new IllegalArgumentException("유효하지 않은 시군구 코드: " + districtCode);
        }

        userRole = userRole.equals("일반") ? "NORMAL" : "REALTOR";

        // 2. 사용자 역할 매핑
        UserRole role = UserRole.fromString(userRole);

        // 3. S3 객체 키 생성
        String objectKey = s3ManagerService.generateObjectKey(districtCode ,role.getRoleName());

        // 4. S3 URL 가져오기
        String podcastUrl = s3ManagerService.getPodcastUrl(objectKey);
        System.out.println("podcastUrl: " + podcastUrl);

        // 5. 참가자 설정
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("호스트1", "호스트"));
        participants.add(new Participant("호스트2", "게스트"));

        // 6. PodcastScript 객체 생성
        PodcastScript podcastScript = new PodcastScript();
        //podcastScript.setScript(null); // 대본은 스케줄러에서 생성되므로 null로 설정
        podcastScript.setParticipants(participants);
        podcastScript.setPodcastUrl(podcastUrl); // S3 URL 설정
        podcastScript.setDistrictName(districtName); //시군구 이름 반환

        return podcastScript;
    }

}
