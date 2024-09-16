package club.pard.exit.actionitem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "Action Item 수정 시 보내는 요청 내용. 수정 시 같은 사용자 아래에 같은 카테고리/이름으로 된 Action Item이 2개 이상 존재할 수 없음")
@Getter
@RequiredArgsConstructor
public class UpdateActionItemRequest {
    @Schema(description = "수정할 Action Item의 새 카테고리, null이거나 비어있을 수 없음")
    @NotBlank(message = "Category should not be null or empty")
    private String category;

    @Schema(description = "수정할 Action Item의 새 내용, null이거나 비어있을 수 없음")
    @NotBlank(message = "Content should not be null or empty")
    private String content;
}
