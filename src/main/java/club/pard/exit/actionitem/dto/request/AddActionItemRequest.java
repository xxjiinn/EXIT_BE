package club.pard.exit.actionitem.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Schema(description = "Action Item 추가 시 보내는 요청 내용. 같은 사용자 아래에 같은 카테고리/내용의 Action Item이 2개 이상 존재할 수 없음")
@Getter
@ToString
@RequiredArgsConstructor
public class AddActionItemRequest {
    @Schema(description = "Action Item 카테고리 / null이거나 비어있을 수 없음")
    @NotBlank(message = "Category should not be null or empty")
    private String category;

    @Schema(description = "Action Item 내용 / null이거나 비어있을 수 없음")
    @NotBlank(message = "Content should not be null or empty")
    private String content;
}
