package club.pard.exit.actionitem.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.pard.exit.actionitem.dto.request.AddActionItemRequest;
import club.pard.exit.actionitem.dto.request.UpdateActionItemRequest;
import club.pard.exit.actionitem.dto.response.Response;
import club.pard.exit.actionitem.dto.response.ActionItemSimplifiedResponse;
import club.pard.exit.actionitem.service.ActionItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "ActionItem", description = "Action Item 관련 API") // swagger 페이지 내의 설명
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/actionItem")
public class ActionItemController {
    private final ActionItemService actionItemService;

    @Operation(summary = "ActionItem/add: Action Item 추가 시 사용하는 API Call", description = "userId를 URL 안에 Path Variable로써 정수 형태로, Action Item의 카테고리와 이름을 각각 Body로써 문자열 형태로 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Action Item이 정상적으로 추가됨을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Action Item 추가 시 카테고리나 내용이 비어 있거나 전송되지 않은 경우, 혹은 같은 카테고리/이름으로 된 Action Item이 존재할 경우, 혹은 사용자가 가질 수 있는 갯수(5개)를 전부 가지고 있을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found: Path Variable로 명시한 User가 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @PostMapping("/{userId}")
    public ResponseEntity<Response<ActionItemSimplifiedResponse>> add(@Parameter(description = "Action Item을 추가할 대상 사용자의 ID", required = true) @PathVariable Long userId, @RequestBody @Valid AddActionItemRequest request)
    {
        return actionItemService.add(userId, request);
    }

    @Operation(summary = "ActionItem/list: 특정 사용자의 Action Item 목록 조회 시 사용하는 API Call", description = "userId를 URL 안에 Path Variable로써 정수 형태로 입력받는다. Body로 받는 내용은 없다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: 특정 사용자의 Action Item을 성공적으로 가져왔음을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: 여기선 안 다루지만 GlobalExceptionHandler에서 전역으로 다루는 Response", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found: Path Variable로 명시한 User가 존재하지 않거나 User 안에 Action Item 리스트가 (비어있는 게 아니라 아예) 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @GetMapping("/{userId}/all")
    public ResponseEntity<Response<List<ActionItemSimplifiedResponse>>> list(@Parameter(description = "Action Item 목록을 조회할 대상 사용자의 ID", required = true) @PathVariable Long userId)
    {
        return actionItemService.list(userId);
    }

    @Operation(summary = "ActionItem/expose: 배너에 특정 사용자의 Action Item을 노출시킬 때 사용하는 API Call", description = "userId를 URL 안에 Path Variable로써 정수 형태로 입력받는다. Body로 받는 내용은 없다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Action Item 하나를 정상적으로 뽑아옴을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: 여기선 안 다루지만 GlobalExceptionHandler에서 전역으로 다루는 Response", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found: Path Variable로 명시한 User가 존재하지 않거나 User 안에 Action Item 리스트가 (비어있는 게 아니라 아예) 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @PatchMapping("/expose/{userId}")
    public ResponseEntity<Response<ActionItemSimplifiedResponse>> expose(@Parameter(description = "Action Item을 배너로 노출시킬 대상 사용자의 ID", required = true) @PathVariable Long userId)
    {
        return actionItemService.expose(userId);
    }

    @Operation(summary = "ActionItem/update: Action Item 수정 시 사용하는 API Call", description = "userId와 itemId를 URL 안에 Path Variable로써 정수 형태로, 바꾸게 될 새 Action Item의 카테고리와 이름을 Body에 문자열로 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Action Item이 정상적으로 수정됨을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Action Item 수정 시 카테고리나 내용이 비어 있거나 전송되지 않은 경우, 혹은 이미 같은 카테고리/이름으로 된 Action Item이 존재할 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden: Path Variable로 명시한 User가 Action Item을 소유하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found: Path Variable로 명시한 User나 Action Item이 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @PatchMapping("/{userId}/{itemId}")
    public ResponseEntity<Response<ActionItemSimplifiedResponse>> update(@Parameter(description = "Action Item을 수정할 대상 사용자의 ID", required = true) @PathVariable Long userId, @Parameter(description = "수정할 대상 Action Item의 ID", required = true) @PathVariable Long itemId, @RequestBody @Valid UpdateActionItemRequest request)
    {
        return actionItemService.update(userId, itemId, request);
    }

    @Operation(summary = "ActionItem/remove: Action Item 삭제 시 사용하는 API Call", description = "userId와 itemId를 URL 안에 Path Variable로써 정수 형태로 입력받는다. Body로 받는 내용은 없다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Action Item이 정상적으로 삭제됨을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: 여기선 안 다루지만 GlobalExceptionHandler에서 전역으로 다루는 Response", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden: Path Variable로 명시한 User가 Action Item을 소유하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found: Path Variable로 명시한 User나 Action Item이 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<Response<?>> remove(@Parameter(description = "Action Item을 삭제할 대상 사용자의 ID", required = true) @PathVariable Long userId, @Parameter(description = "삭제할 대상 Action Item의 ID", required = true) @PathVariable Long itemId)
    {
        return actionItemService.remove(userId, itemId);
    }
}

