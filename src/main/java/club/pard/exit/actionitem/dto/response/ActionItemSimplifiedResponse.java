package club.pard.exit.actionitem.dto.response;

import club.pard.exit.actionitem.entity.ActionItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "ActionItem에서 User 정보를 간소화한 Response")
@Getter
@RequiredArgsConstructor
public class ActionItemSimplifiedResponse {
    @Schema(description = "Action Item의 ID")
    private final Long id;

    @Schema(description = "Action Item의 카테고리", example = "category_name")
    private final String category;

    @Schema(description = "Action Item의 내용", example = "item_content")
    private final String content;

    @Schema(description = "Action Item이 배너로 노출된 횟수")
    private final Long exposureCount;

    public static ActionItemSimplifiedResponse from(ActionItem actionItem){
        return new ActionItemSimplifiedResponse(actionItem.getId(), actionItem.getCategory(), actionItem.getContent(), actionItem.getExposureCount());
    }
}

