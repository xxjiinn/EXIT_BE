package club.pard.exit.ejection.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.pard.exit.actionitem.dto.response.Response;
import club.pard.exit.ejection.dto.response.EjectionRankResponse;
import club.pard.exit.ejection.dto.response.ListEjectionRankResponse;
import club.pard.exit.ejection.entity.Ejection;
import club.pard.exit.user.entity.User;

import club.pard.exit.ejection.repo.EjectionRepo;
import club.pard.exit.ejection.entity.EjectionRank;
import club.pard.exit.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EjectionService {
    private final EjectionRepo ejectionRepository;
    private final UserRepo userRepository;

    @Transactional
    public ResponseEntity<Response<?>> add(Long userId)
    {
        try
        {
            User targetUser = userRepository.findById(userId).orElse(null);
            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "Ejection/add: Target User not existent"));


            Ejection newEjection = new Ejection();
            ejectionRepository.save(newEjection);
            targetUser.addEjection(newEjection);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Response.setSuccess("탈출 추가 완료!", "Ejection/add: Successful", null));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버에 오류가 생겼어요!", "Ejection/add: Internal Server Error"));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Response<ListEjectionRankResponse>> getRank(Long userId, Timestamp targetTimestamp)
    {
        try
        {
            List<EjectionRank> rankRaw = ejectionRepository.getRankList(targetTimestamp);
            List<EjectionRankResponse> rank = new ArrayList<>();
            Long targetUserRank = null;
            Long targetUserEjectionCount = null;
            String targetUserNickname = null;

            for(EjectionRank each: rankRaw)
            {
                if(Objects.equals(each.getUserId(), userId))
                {
                    targetUserRank = each.getRank();
                    targetUserNickname = userRepository.findById(each.getUserId()).get().getNickname();
                    targetUserEjectionCount = each.getEjectionCount();
                }
                if(rank.size() < 10)
                {
                    String nickname = userRepository.findById(each.getUserId()).get().getNickname();
                    // String nickname = each.getUserNickname();
                    Long ejectionRank = each.getRank();
                    Long ejectionCount = each.getEjectionCount();
                    rank.add(new EjectionRankResponse(ejectionRank, nickname, ejectionCount));
                }
                else if(targetUserRank != null) break;
            }
            EjectionRankResponse myRank = EjectionRankResponse.of(targetUserRank, targetUserNickname, targetUserEjectionCount);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("탈출 순위표 조회 완료!", "Ejection/getRank: Successful", ListEjectionRankResponse.of(userId, rank, myRank)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버에 오류가 생겼어요!", "Ejection/getRank: Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Response<Long>> getTodays(Long userId, Timestamp targetTimestamp)
    {
        try
        {
            if(!userRepository.existsById(userId))
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "Ejection/getTodays: Target User not existent"));
            Long ejections = ejectionRepository.countByUserIdAndTimeOccurredGreaterThanEqual(userId, targetTimestamp);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("탈출 목록 조회 완료!", "Ejection/getTodays: Successful", ejections));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버에 오류가 생겼어요!", "Ejection/getTodays: Internal Server Error"));
        }
    }
}
