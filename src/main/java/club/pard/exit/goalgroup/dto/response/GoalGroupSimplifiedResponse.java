package club.pard.exit.goalgroup.dto.response;

import club.pard.exit.goalgroup.entity.GoalGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "목표 그룹에서 User 정보를 간소화한 Response")
@Getter
@Setter
@NoArgsConstructor
public class GoalGroupSimplifiedResponse {
    @Schema(description = "목표 그룹 ID")
    private Long id;

    @Schema(description = "목표 그룹 소유자의 사용자 ID 번호")
    private Long userId;

    @Schema(description = "목표 그룹 제목", example = "goal_group_title")
    private String title;

    // // TODO: revise GoalGroup so that apps list could be added
    // @Schema(description = "목표 그룹에 속한 앱들의 목록", example = "{\"appId_1\", \"appId_2\", ...}")
    // private List<String> apps;

    @Schema(description = "목표 그룹에서 정한 시간 제한(분 단위)")
    private Long timeBudget;

    @Schema(description = "목표 그룹에서 정한 알림 간격 시간(분 단위)")
    private Long nudgeInterval;

    public static GoalGroupSimplifiedResponse from(GoalGroup goalGroup){
        GoalGroupSimplifiedResponse response = new GoalGroupSimplifiedResponse();
        response.setId(goalGroup.getId());
        response.setUserId(goalGroup.getUser().getId());
        response.setTitle(goalGroup.getTitle());
        response.setTimeBudget(goalGroup.getTimeBudget());
        response.setNudgeInterval(goalGroup.getNudgeInterval());

        // // TODO: revise GoalGroup so that apps list could be added
        // response.setApps(new ArrayList<>());
        // for(Goal goal: goalGroup.getGoals())
        //     response.getApps().add(goal.getAppId());

        return response;
    }
}
