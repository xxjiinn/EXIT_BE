package club.pard.exit.phoneusage.controller;

import club.pard.exit.actionitem.dto.response.Response;
import club.pard.exit.phoneusage.dto.request.AddPhoneUsageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.pard.exit.phoneusage.dto.response.*;
import club.pard.exit.phoneusage.service.PhoneUsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "PhoneUsage", description = "폰 사용 시간 관련 API")
@RestController
@RequestMapping("/api/phoneUsage")
@RequiredArgsConstructor
public class PhoneUsageController {
    private final PhoneUsageService phoneUsageService;

    @Operation(summary = "PhoneUsage/add: 사용 시간 기록 시 사용하는 API Call", description = "userId를 URL 안에 Path Variable로써 정수 형태로, 사용 시간 기록을 Body로 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: 사용 시간이 정상적으로 기록됨을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: 기록하는 날짜가 비어 있거나 오늘을 넘어가는 미래의 시점일 경우, 혹은 사용한 시간이 존재하지 않거나 음수일 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found: Path Variable로 명시한 User가 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @PutMapping("/{userId}")
    public ResponseEntity<Response<PhoneUsageSimplifiedResponse>> add(@PathVariable Long userId, @RequestBody @Valid AddPhoneUsageRequest request)
    {
        return phoneUsageService.add(userId, request);
    }

    @Operation(summary = "PhoneUsage/getAnalysisDaily: 전날과 당일의 사용 시간을 받을 때 사용하는 API Call", description = "userId를 URL 안에 Path Variable로써 정수 형태로, 당일의 날짜를 YYYY-mm-dd 형식으로 Parameter로 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: 전날과 당일의 사용 시간을 정상적으로 가져옴을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Parameter로 받은 날짜가 비어 있거나 오늘을 넘어가는 미래의 시점일 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found: Path Variable로 명시한 User가 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @GetMapping("/{userId}/daily")
    public ResponseEntity<Response<PhoneUsageComparisonDailyResponse>> getAnalysisDaily(@PathVariable Long userId, @RequestParam("date") String date)
    {
        return phoneUsageService.getAnalysisDaily(userId, date);
    }

    @Operation(summary = "PhoneUsage/getAnalysisWeekly: 당일 기준으로 그 주의 월요일부터 당일까지의 사용 시간을 받을 때 사용하는 API Call", description = "userId를 URL 안에 Path Variable로써 정수 형태로, 당일의 날짜를 YYYY-mm-dd 형식으로 Parameter로 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: 전날과 당일의 사용 시간을 정상적으로 가져옴을 의미함"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Parameter로 받은 날짜가 비어 있거나 오늘을 넘어가는 미래의 시점일 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found: Path Variable로 명시한 User가 존재하지 않을 경우 발생함", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: 서버에 다루지 못한 Exception이 발생, 예외 스택이 출력될 것이니 서버 관리자가 확인해야 함", content = @Content)
    })
    @GetMapping("/{userId}/weekly")
    public ResponseEntity<Response<PhoneUsageComparisonWeeklyResponse>> getAnalysisWeekly(@PathVariable Long userId, @RequestParam("date") String date)
    {
        return phoneUsageService.getAnalysisWeekly(userId, date);
    }
}
