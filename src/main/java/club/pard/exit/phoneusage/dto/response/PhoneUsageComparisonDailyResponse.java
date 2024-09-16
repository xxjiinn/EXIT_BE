package club.pard.exit.phoneusage.dto.response;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "전날 - 당일 간 사용 시간 비교 시 받게 되는 Response")
@Getter
@RequiredArgsConstructor
public class PhoneUsageComparisonDailyResponse {
    @Schema(description = "사용 시간 기록 소유자의 사용자 ID 번호")
    private final Long userId;

    @Schema(description = "비교하는 날짜 중 당일의 날짜")
    private final Date date;

    @Schema(description = "비교하는 날짜 중 전날에 사용한 시간(분 단위)")
    private final Long timeUsedYesterday;

    @Schema(description = "비교하는 날짜 중 당일에 사용한 시간(분 단위)")
    private final Long timeUsedToday;

    @Schema(description = "당일의 (전날과 비교한) 사용 시간 변화량(백분율)")
    private final Long timeUsedDifferenceRate;

    public static PhoneUsageComparisonDailyResponse of(Long userId, Date date, Long timeUsedYesterday, Long timeUsedToday, Long timeUsedDifferenceRate){ return new PhoneUsageComparisonDailyResponse(userId, date, timeUsedYesterday, timeUsedToday, timeUsedDifferenceRate); }
}

