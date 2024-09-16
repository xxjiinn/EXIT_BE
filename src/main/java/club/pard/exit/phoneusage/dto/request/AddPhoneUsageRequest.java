package club.pard.exit.phoneusage.dto.request;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "사용 시간 기록 시 보내는 요청 내용")
@Getter
@ToString
@NoArgsConstructor
public class AddPhoneUsageRequest {
    @Schema(description = "추가할 사용 시간의 날짜. null이거나 미래의 날짜일 수 없음")
    @NotNull @PastOrPresent
    private Date date;

    @Schema(description = "추가할 사용 시간(분 단위). null이거나 음수일 수 없음")
    @NotNull @PositiveOrZero
    private Long timeUsed;
}

