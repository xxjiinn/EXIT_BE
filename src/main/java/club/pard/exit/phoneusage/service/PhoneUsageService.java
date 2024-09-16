package club.pard.exit.phoneusage.service;

import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import club.pard.exit.phoneusage.repo.PhoneUsageRepo;
import club.pard.exit.user.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.pard.exit.phoneusage.dto.request.AddPhoneUsageRequest;
import club.pard.exit.actionitem.dto.response.Response;
import club.pard.exit.phoneusage.dto.response.*;
import club.pard.exit.phoneusage.entity.PhoneUsage;
import club.pard.exit.user.entity.User;
import club.pard.exit.phoneusage.repo.PhoneUsageRepo;
import club.pard.exit.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoneUsageService {
    private final PhoneUsageRepo phoneUsageRepository;
    private final UserRepo userRepository;

    @Transactional
    public ResponseEntity<Response<PhoneUsageSimplifiedResponse>> add(Long userId, AddPhoneUsageRequest request)
    {
        try
        {
            User targetUser = userRepository.findById(userId).orElse(null);
            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "PhoneUsage/add: Target User not existent"));

            Date date = request.getDate();
            Long timeUsed = request.getTimeUsed();

            if(date == null || date.compareTo(new Date(System.currentTimeMillis())) > 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("사용 날짜가 존재하지 않거나 지나가지 않은 날짜에요!", "PhoneUsage/add: Usage Date is null or of future time"));
            if(timeUsed == null || timeUsed < 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("사용 시간이 존재하지 않거나 음수에요!", "PhoneUsage/add: Usage time is null or negative"));

            PhoneUsage phoneUsage = phoneUsageRepository.findByUserIdAndDate(timeUsed, date).orElse(null);
            if(phoneUsage == null)
            {
                phoneUsage = PhoneUsage.builder().date(date).timeUsed(timeUsed).build();
                targetUser.addPhoneUsage(phoneUsage);
                phoneUsageRepository.save(phoneUsage);
            }
            else
                phoneUsage.setTimeUsed(timeUsed);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("사용 기록 추가 완료!", "PhoneUsage/add: Successful", PhoneUsageSimplifiedResponse.from(phoneUsage)));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "PhoneUsage/add: Internal Server Error"));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Response<PhoneUsageComparisonDailyResponse>> getAnalysisDaily(Long userId, String targetDateString)
    {
        try
        {
            Date targetDate = Date.valueOf(targetDateString);

            User targetUser = userRepository.findById(userId).orElse(null);
            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "PhoneUsage/getAnalysisDaily: Target User not existent"));

            if(targetDate == null || targetDate.compareTo(new Date(System.currentTimeMillis())) > 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("사용 날짜를 확인할 수 없거나 지나가지 않은 날짜에요!", "PhoneUsage/getAnalysisDaily: Date is null or of future time"));

            Date yesterday = Date.valueOf(targetDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalDate()
                    .minusDays(1));

            PhoneUsage phoneUsageToday = phoneUsageRepository.findByUserIdAndDate(userId, targetDate).orElse(null);
            Long timeUsedToday = (phoneUsageToday == null) ? 0 : phoneUsageToday.getTimeUsed();

            PhoneUsage phoneUsageYesterday = phoneUsageRepository.findByUserIdAndDate(userId, yesterday).orElse(null);
            Long timeUsedYesterday = (phoneUsageYesterday == null) ? 0 : phoneUsageYesterday.getTimeUsed();

            Long timeUsedDifferenceRate = Math.round((timeUsedToday - timeUsedYesterday) / timeUsedYesterday.doubleValue() * 100);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("어제 오늘 사용 시간 조회 성공!", "PHoneUsage/getAnalysisDaily: Successful", new PhoneUsageComparisonDailyResponse(userId, targetDate, timeUsedYesterday, timeUsedToday, timeUsedDifferenceRate)));
        }
        catch(Exception e)
        {
            e.printStackTrace();;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "PhoneUsage/getAnalysisDaily: Internal Server Error"));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Response<PhoneUsageComparisonWeeklyResponse>> getAnalysisWeekly(Long userId, String targetDateString)
    {
        try
        {
            Date targetDate = Date.valueOf(targetDateString);

            User targetUser = userRepository.findById(userId).orElse(null);
            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "PhoneUsage/getAnalysisDaily: Target User not existent"));

            if(targetDate == null || targetDate.compareTo(new Date(System.currentTimeMillis())) > 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("사용 날짜를 확인할 수 없거나 지나가지 않은 날짜에요!", "PhoneUsage/getAnalysisWeekly: Date is null or of future time"));

            List<Long> timeUsedWeekly = new ArrayList<>();
            int dayOfWeek = targetDate.toLocalDate().getDayOfWeek().getValue();
            for(int day = (dayOfWeek - 1); day >= 0; day --)
            {
                Date dateWeekly = Date.valueOf(targetDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalDate()
                        .minusDays(Long.valueOf(day)));
                PhoneUsage usageWeekly = phoneUsageRepository.findByUserIdAndDate(userId, dateWeekly).orElse(null);
                timeUsedWeekly.add((usageWeekly == null) ? 0 : usageWeekly.getTimeUsed());
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("주간 사용 시간 조회 성공!", "PhoneUsage/getAnalysisWeekly: Successful", new PhoneUsageComparisonWeeklyResponse(userId, targetDate, timeUsedWeekly)));
        }
        catch(Exception e)
        {
            e.printStackTrace();;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "PhoneUsage/getAnalysisDaily: Internal Server Error"));
        }
    }
}

