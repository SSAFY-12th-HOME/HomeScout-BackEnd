package com.ssafy.homescout.chat.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * LocalDateTime을 [오전 or 오후 hh:mm] 형식의 문자열로 직렬화하는 커스텀 직렬화기
 */
public class CustomDateSerializer extends JsonSerializer<LocalDateTime> {

    // Locale.KOREAN을 사용하여 "오전/오후" 표시를 한글로 처리
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN);

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        // LocalDateTime을 지정된 포맷으로 변환
        String formattedDate = value.format(formatter);

        // JSON 문자열로 작성
        gen.writeString(formattedDate);
    }
}
