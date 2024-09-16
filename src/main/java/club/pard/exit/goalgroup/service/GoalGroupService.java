package club.pard.exit.goalgroup.service;

import java.util.ArrayList;
// import java.util.HashMap;
import java.util.List;
// import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.pard.exit.goalgroup.dto.request.*;
import club.pard.exit.actionitem.dto.response.Response;
import club.pard.exit.goalgroup.dto.response.GoalGroupSimplifiedResponse;
import club.pard.exit.goalgroup.entity.*;
import club.pard.exit.user.entity.User;
import club.pard.exit.goalgroup.repo.GoalGroupRepo;
import club.pard.exit.goalgroup.repo.GoalRepo;
import club.pard.exit.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalGroupService {
    private final GoalGroupRepo goalGroupRepository;
    private final UserRepo userRepository;
    // // TODO: revise GoalGroup so that apps list could be added
    // private final GoalRepository goalRepository;

    @Transactional
    public ResponseEntity<Response<GoalGroupSimplifiedResponse>> add(Long userId, AddGoalGroupRequest request)
    {
        try
        {
            User targetUser = userRepository.findById(userId).orElse(null);
            String groupTitle = request.getTitle();
            Long timeBudget = request.getTimeBudget();
            Long nudgeInterval = request.getNudgeInterval();
            // // TODO: revise GoalGroup so that apps list could be added
            // List<String> apps = request.getApps();

            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "GoalGroup/add: Target User not existent"));

            // Not likely gonna happen here since those fields would be validated elsewhere
            if(groupTitle == null || groupTitle.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("그룹 이름이 비어 있어요!", "GoalGroup/add: Goal Group title is empty or null"));
            if(timeBudget == null || timeBudget <= 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("목표 설정 시간이 존재하지 않거나 양의 정수가 아니에요!", "GoalGroup/add: Time budget is null or not positive"));
            if(nudgeInterval == null || nudgeInterval <= 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("알림 간격 시간이 존재하지 않거나 양의 정수가 아니에요!", "GoalGroup/add: Nudge interval is null or not positive"));

            // // TODO: revise GoalGroup so that apps list could be added
            // if(apps == null)
            //     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            //         .body(Response.setFailure("그룹에 속하는 어플리케이션 리스트가 존재하지 않아요!", "GoalGroup/add: Apps list can be empty but should not be null"));
            //
            // boolean appAlreadyExistentOnAnotherGoalGroup = false;
            // for(String app: apps)
            // {
            //     if(goalRepository.existsByUserIdAndAppId(userId, app))
            //     {
            //         appAlreadyExistentOnAnotherGoalGroup = true;
            //         break;
            //     }
            // }
            // if(appAlreadyExistentOnAnotherGoalGroup)
            //     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            //         .body(Response.setFailure("이미 다른 그룹에 선택된 앱이 있어요!", "GoalGroup/add: One of selected apps already existent in another group"));

            if(targetUser.getGoalGroups().size() >= 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("사용자의 Goal Group 갯수 제한(1개)를 이미 채웠어요!", "GoalGroup/add: Target User already has reached Goal Group number of 1"));

            GoalGroup newGoalGroup = GoalGroup.builder().title(groupTitle).timeBudget(timeBudget).nudgeInterval(nudgeInterval).build();
            targetUser.addGoalGroup(newGoalGroup);
            goalGroupRepository.save(newGoalGroup);

            // // TODO: revise GoalGroup so that apps list could be added
            // for(String app: apps)
            // {
            //     Goal newGoal = Goal.builder().appId(app).timeBudget(timeBudget).build();
            //     newGoalGroup.addGoal(newGoal);
            //
            //     goalRepository.save(newGoal);
            // }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("그룹과 목표 추가 완료!", "GoalGroup/add: Successful", GoalGroupSimplifiedResponse.from(newGoalGroup)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버에 오류가 생겼어요!", "GoalGroup/add: Internal Server Error"));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Response<List<GoalGroupSimplifiedResponse>>> list(Long userId)
    {
        try
        {
            User yser = userRepository.findById(userId).orElse(null);
            List<GoalGroup> goalGroups = goalGroupRepository.findAllByUserId(userId);

            if(yser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "GoalGroup/list: Target User not existent"));
            if(goalGroups == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자의 목표 그룹 리스트가 존재하지 않아요!", "GoalGroup/list: Goal Group list from target User is null"));

            List<GoalGroupSimplifiedResponse> goalGroupsSimplified = new ArrayList<>();
            for(GoalGroup goalGroup: goalGroups)
                goalGroupsSimplified.add(GoalGroupSimplifiedResponse.from(goalGroup));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("목표 그룹 조회 완료!", "GoalGroup/list: Successful", goalGroupsSimplified));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버에 오류가 생겼어요!", "GoalGroup/list: Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Response<GoalGroupSimplifiedResponse>> update(Long userId, Long goalGroupId, UpdateGoalGroupRequest request)
    {
        try
        {
            String newTitle = request.getTitle();
            // List<String> newApps = request.getApps();
            Long newTimeBudget = request.getTimeBudget();
            Long newNudgeInterval = request.getNudgeInterval();

            User targetUser = userRepository.findById(userId).orElse(null);
            GoalGroup targetGoalGroup = goalGroupRepository.findById(goalGroupId).orElse(null);

            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "GoalGroup/update: Target User not existent"));
            if(targetGoalGroup == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 목표 그룹이 존재하지 않아요!", "GoalGroup/update: Target Goal Group not existent"));
            if(targetGoalGroup.getUser().getId() != userId)
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Response.setFailure("해당 사용자가 해당 목표 그룹을 소유하지 않아요!", "GoalGroup/update: Target User does not own target Goal Group"));

            // Not likely gonna happen since those fields will be validated elsewhere
            if(newTitle == null || newTitle.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("그룹 이름이 비어 있어요!", "GoalGroup/update: Goal Group title is null or empty"));
            if(newTimeBudget == null || newTimeBudget < 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("목표 설정 시간은 존재하지 않거나 음수일 수 없어요!", "GoalGroup/update: Time budget cannot be null or negative"));
            if(newNudgeInterval == null || newNudgeInterval < 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("알림 간격 시간은 존재하지 않거나 음수일 수 없어요!", "GoalGroup/update: Nudge interval cannot be null or negative"));

            // // TODO: revise GoalGroup so that apps list could be added
            // if(newApps == null)
            //     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            //         .body(Response.setFailure("그룹에 속하는 어플리케이션 리스트가 존재하지 않아요!", "GoalGroup/update: Apps list can be empty but should not be null"));


            targetGoalGroup.setTitle(newTitle);
            targetGoalGroup.setTimeBudget(newTimeBudget);
            targetGoalGroup.setNudgeInterval(newNudgeInterval);


            // // TODO: revise GoalGroup so that apps list could be added
            // Map<String, Goal> goalMap = new HashMap<>();
            //
            // List<String> originalApps = new ArrayList<>();
            // for(Goal goal: targetGoalGroup.getGoals())
            // {
            //     originalApps.add(goal.getAppId());
            //     goalMap.put(goal.getAppId(), goal);
            // }
            //
            // for(String app: newApps)
            // {
            //     if(originalApps.contains(app))
            //     {
            //         goalMap.get(app).setTimeBudget(newTimeBudget);
            //         goalMap.get(app).setNudgeInterval(newNudgeInterval);
            //     }
            //     else
            //     {
            //         Goal newGoal = Goal.builder().appId(app).timeBudget(newTimeBudget).nudgeInterval(newNudgeInterval).build();
            //         goalMap.put(app, newGoal);
            //         targetGoalGroup.addGoal(newGoal);
            //     }
            // }
            // for(String app: originalApps)
            // {
            //     if(!newApps.contains(app))
            //     {
            //         targetGoalGroup.removeGoal(goalMap.get(app));
            //         goalMap.remove(app);
            //     }
            // }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("목표 그룹 수정 완료!", "GoalGroup/update: Successful", GoalGroupSimplifiedResponse.from(targetGoalGroup)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버에 오류가 생겼어요!", "GoalGroup/update: Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Response<?>> remove(Long userId, Long goalGroupId)
    {
        try
        {
            User targetUser = userRepository.findById(userId).orElse(null);
            GoalGroup targetGoalGroup = goalGroupRepository.findById(goalGroupId).orElse(null);

            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!","GoalGroup/remove: Target User not existent"));
            if(targetGoalGroup == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 목표 그룹이 존재하지 않아요!", "GoalGroup/remove: Target Goal Group not existent"));

            if(targetGoalGroup.getUser().getId() != userId)
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Response.setFailure("해당 사용자가 해당 목표 그룹을 소유하지 않아요!", "GoalGroup/remove: Target User does not own target Goal Group"));

            // // TODO: revise GoalGroup so that apps list could be added
            // for(Goal goal: targetGoalGroup.getGoals())
            //     targetGoalGroup.removeGoal(goal);
            targetUser.removeGoalGroup(targetGoalGroup);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("목표 그룹 삭제 완료!", "GoalGroup/remove: Successful", null));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버에 오류가 생겼어요!", "Internal DB Error"));
        }
    }
}

