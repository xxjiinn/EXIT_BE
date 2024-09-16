package club.pard.exit.phoneusage.dto.response;

import java.sql.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "당일 기준으로 한 주(~당일까지) 동안 사용 시간 비교 시 받게 되는 Response")
@Getter
@AllArgsConstructor
public class PhoneUsageComparisonWeeklyResponse {
    @Schema(description = "사용 시간 기록 소유자의 사용자 ID 번호")
    private final Long userId;

    @Schema(description = "사용 시간 기록의 날짜")
    private final Date date;

    @Schema(description = "월요일부터 당일까지 사용한 사용 시간(분 단위)의 목록")
    private final List<Long> timesUsedWeekly;
}

