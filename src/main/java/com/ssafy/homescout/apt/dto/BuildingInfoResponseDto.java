package com.ssafy.homescout.apt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class BuildingInfoResponseDto {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
        private String numOfRows;
        private String pageNo;
        private String totalCount;
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<Item> item;
    }

    @Data
    public static class Item {
        private int rnum;
        private String platPlc;
        private String sigunguCd;
        private String bjdongCd;
        private String platGbCd;
        private String bun;
        private String ji;
        private long mgmBldrgstPk;
        private String regstrGbCd;
        private String regstrGbCdNm;
        private String regstrKindCd;
        private String regstrKindCdNm;
        private String newPlatPlc;
        private String bldNm;
        private String splotNm;
        private String block;
        private String lot;
        private int bylotCnt;
        private String naRoadCd;
        private String naBjdongCd;
        private String naUgrndCd;
        private String naMainBun;
        private String naSubBun;
        private String dongNm;
        private String mainAtchGbCd;
        private String mainAtchGbCdNm;
        private double platArea;
        private double archArea;
        private double bcRat;
        private double totArea;
        private double vlRatEstmTotArea;
        private double vlRat;
        private String strctCd;
        private String strctCdNm;
        private String etcStrct;
        private String mainPurpsCd;
        private String mainPurpsCdNm;
        private String etcPurps;
        private String roofCd;
        private String roofCdNm;
        private String etcRoof;
        private int hhldCnt;
        private int fmlyCnt;
        private int heit;
        private int grndFlrCnt;
        private int ugrndFlrCnt;
        private int rideUseElvtCnt;
        private int emgenUseElvtCnt;
        private int atchBldCnt;
        private double atchBldArea;
        private double totDongTotArea;
        private int indrMechUtcnt;
        private double indrMechArea;
        private int oudrMechUtcnt;
        private double oudrMechArea;
        private int indrAutoUtcnt;
        private double indrAutoArea;
        private int oudrAutoUtcnt;
        private double oudrAutoArea;
        private String pmsDay;
        private String stcnsDay;
        private String useAprDay;
        private String pmsnoYear;
        private String pmsnoKikCd;
        private String pmsnoKikCdNm;
        private String pmsnoGbCd;
        private String pmsnoGbCdNm;
        private int hoCnt;
        private String engrGrade;
        private int engrRat;
        private int engrEpi;
        private String gnBldGrade;
        private int gnBldCert;
        private String itgBldGrade;
        private int itgBldCert;
        private String crtnDay;
        private String rserthqkDsgnApplyYn;
        private String rserthqkAblty;
    }
}