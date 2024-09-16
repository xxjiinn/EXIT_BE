package club.pard.exit.ejection.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "당일 탈출 횟수 순위 정보가 담아져 있는 Response")
@Getter
@RequiredArgsConstructor
public class ListEjectionRankResponse {
    @Schema(description = "탈출 순위를 조회하는 사용자의 ID 번호")
    private final Long userId;

    @Schema(description = "1 ~ 10위의 탈출 순위 정보")
    private final List<EjectionRankResponse> ejectionRanks;

    @Schema(description = "사용자의 탈출 순위 정보")
    private final EjectionRankResponse myEjectionRank;

    public static ListEjectionRankResponse of(Long userId, List<EjectionRankResponse> ejectionRanks, EjectionRankResponse myEjectionRank){
        return new ListEjectionRankResponse(userId, ejectionRanks, myEjectionRank); }
}
