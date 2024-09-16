package club.pard.exit.ejection.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "당일 탈출 횟수 순위 정보가 담아져 있는 Response")
@Getter
@RequiredArgsConstructor
public class EjectionRankResponse {
    @Schema(description = "탈출 기록 순위 번호")
    private final Long rank;

    @Schema(description = "탈출 기록 소유자의 별명", example = "잘난뻐꾸기9999")
    private final String nickname;

    @Schema(description = "(당일의) 탈출 횟수")
    private final Long ejectionCount;

    public static EjectionRankResponse of(Long rank, String nickname, Long ejectionCount){ return new EjectionRankResponse(rank, nickname, ejectionCount); }
}
