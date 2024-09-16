package club.pard.exit.actionitem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "데이터를 담는 Response 템플릿")
@Data
@AllArgsConstructor(staticName = "set")
public class Response<D> {
    @Schema(description = "요청이 성공적으로 이루어졌는지의 여부", example = "true")
    private boolean isSuccessful;   // Basic boolean value for call result

    @Schema(description = "클라이언트 쪽에서 확인하기 위한 메세지", example = "요쳥 결과 메세지")
    private String message;         // For client developers

    @Schema(description = "서버 쪽에서 확인하기 위한 메세지(주석)", example = "{Entity}/{Action}: {message}")
    private String comment;         // For server developers

    @Schema(description = "서버에서 받은 데이터")
    private D data;                 // What client will receive after request

    public static <D> Response<D> setSuccess(String message, String comment, D data){ return Response.set(true,     message, comment, data); }
    public static <D> Response<D> setFailure(String message, String comment)        { return Response.set(false,    message, comment, null); }
}
