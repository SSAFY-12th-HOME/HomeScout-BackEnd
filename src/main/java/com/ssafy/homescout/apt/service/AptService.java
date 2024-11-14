package com.ssafy.homescout.apt.service;

import com.ssafy.homescout.api.service.WebClientService;
import com.ssafy.homescout.apt.dto.*;
import com.ssafy.homescout.apt.mapper.AptMapper;
import com.ssafy.homescout.entity.*;
import com.ssafy.homescout.util.ChronoUtil;
import com.ssafy.homescout.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AptService {

    private final AptMapper aptMapper;
    private final WebClientService webClientService;

    public List<AptPosResponseDto> getAptAll() {
        return aptMapper.selectAllAptPos();
    }

    public AptResponseDto getAptInfo(String aptId) {
        // 아파트 정보 불러오기 (apt 테이블에서)
        Apt apt = aptMapper.selectAptByAptId(aptId);
        if(apt == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 아파트입니다.");
        }

        // 서울특별시 서초구 <- 시도, 시군구 불러오기
        Dongcode dongcode = aptMapper.selectDongcodeByDongCd(apt.getRoadNmSggCd() + "00000");
        // 주소 만들기
        String aptAddress = dongcode.getSidoNm() + " " + dongcode.getSggNm() + " "
                + apt.getRoadNm() + " " + apt.getRoadNmBonbun() + "-" + apt.getRoadNmBubun();
        // 아파트 정보 초기화
        // TODO 용적률, 최고층, 세대수 등 API 받아오기
        AptInfo aptInfo = AptInfo.builder()
                .aptNm(apt.getAptNm())
                .address(aptAddress)
                .homeCnt(0) // 세대수
                .buildYear(apt.getBuildYear())
                .maxFloor(20) // 최고층
                .far(220) // 용적률
                .bcr(15) // 건폐율
                .dongCnt(12) // 동 개수
                .latitude(apt.getLatitude())
                .longitude(apt.getLongitude())
                .build();

        // 매물 정보 불러오기
        List<AptSaleInfo> aptSaleInfo = aptMapper.selectAptSalesByAptId(aptId);
        aptSaleInfo.forEach(o -> {
            o.setPrice(NumberUtil.convertPrice(o.getPrice()));
            o.setDeposit(NumberUtil.convertPrice(o.getDeposit()));
            o.setRentalFee(NumberUtil.convertPrice(o.getRentalFee()));
        });

        // 거래 내역 불러오기 (최근 5개만 불러오게 해놈)
        List<AptDeal> aptDealList = aptMapper.selectAptDealsByAptId(aptId);
        List<AptDealInfo> aptDealInfoList = new ArrayList<>();
        for (AptDeal aptDeal : aptDealList) {
            String dealDate = String.format("%d.%02d.%02d",
                            aptDeal.getDealYear()%2000,
                            aptDeal.getDealMonth(),
                            aptDeal.getDealDay());

            aptDealInfoList.add(AptDealInfo.builder()
                    .dealDate(dealDate)
                    .area(aptDeal.getArea() + "㎡")
                    .price(NumberUtil.convertPrice(aptDeal.getDealAmount().replace(",","")))
                    .build());
        }

        // 반경 1km 내 지하철역 구하기
        List<Subway> subwayList = aptMapper.selectSubwaysByLatLng(apt.getLatitude(), apt.getLongitude());
        List<AptSubwayInfo> aptSubwayInfoList = new ArrayList<>();
        for (Subway subway : subwayList) {
            // 09호선 -> 9호선 바꿔주기
            String lineNm = subway.getLineNm().charAt(0) == '0' ? subway.getLineNm().substring(1) : subway.getLineNm();
            aptSubwayInfoList.add(AptSubwayInfo.builder()
                    .station(subway.getStationNm())
                    .lineNm(lineNm)
                    .color("#e36d12") // TODO 나중에 지하철 노선별 색 데이터 map에 넣고 빼서 쓰기 ex. map.get("9호선")
                    .dist(subway.getDistance().intValue())
                    .walk(subway.getDistance().intValue() / 100)
                    .lat(subway.getLat())
                    .lng(subway.getLng())
                    .build());
        }

        // 살아온 이야기 불러오기 (최대 3개 불러오기)
        List<AptLifeStory> aptLifeStoryList = aptMapper.selectAptLifeStoriesByAptId(aptId);
        // 작성날짜 형식 변경
        aptLifeStoryList.forEach(o -> o.setCreated(ChronoUtil.timesAgo(o.getCreatedAt())));

        return AptResponseDto.of(aptId, aptInfo, aptSaleInfo, aptDealInfoList, aptSubwayInfoList, aptLifeStoryList);
    }

    public LifeStoryResponseDto writeLifeStory(String aptId, Long userId, LifeStoryRequestDto lifeStoryRequestDto) {
        LifeStory lifeStory = LifeStory.builder()
                .aptId(aptId)
                .userId(userId)
                .content(lifeStoryRequestDto.getContent())
                .build();

        aptMapper.insertLifeStory(lifeStory);

        return aptMapper.selectLifeStoryById(lifeStory.getLifeStoryId());
    }

    public AptPosResponseDto findAptByAptNm(String aptNm) {
        List<AptPosResponseDto> aptPosResponseDtoList = aptMapper.selectAptPosByAptNm(aptNm);
        if(aptPosResponseDtoList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일치하는 아파트 정보가 없습니다.");
        }
        return aptPosResponseDtoList.get(0);
    }

    public List<SidoResponseDto> getSido() {
        return aptMapper.getSido();
    }

    public List<GuResponseDto> getGu(String sidoCode) {
        return aptMapper.getGu(sidoCode.substring(0, 2));
    }

    public List<DongResponseDto> getDong(String guCode) {
        return aptMapper.getDong(guCode.substring(0, 5));
    }

    public Object getPosByDongCode(String dongCode) {
        // 동코드로 주소 가져오기 (ex. 서울특별시 서초구 잠원동)
        Dongcode dongCodeEntity = aptMapper.selectDongcodeByDongCd(dongCode);
        String address = dongCodeEntity.getSidoNm() + " " + dongCodeEntity.getSggNm() + " " + dongCodeEntity.getDongNm();

        // 카카오 API 요청
        Map<String, Object> response = webClientService.getPosByAddress(address);

        // 메타데이터: 데이터 반환 개수가 0개면 오류
        Map<String, Object> meta = (Map<String, Object>) response.get("meta");
        if((Integer) meta.get("total_count") == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일치하는 행정동 좌표가 없습니다.");
        }

        // 첫 번째 주소 데이터 가져오기
        Map<String, Object> documents = ((List<Map<String, Object>>) response.get("documents")).get(0);

        // 위도, 경도 데이터 뽑아서 DTO로 변환
        return RegionPosResponseDto.of((String) documents.get("y"), (String) documents.get("x"));
    }
}
