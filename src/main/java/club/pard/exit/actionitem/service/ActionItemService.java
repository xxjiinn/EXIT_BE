package club.pard.exit.actionitem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.pard.exit.actionitem.dto.request.AddActionItemRequest;
import club.pard.exit.actionitem.dto.request.UpdateActionItemRequest;
import club.pard.exit.actionitem.dto.response.Response;
import club.pard.exit.actionitem.dto.response.ActionItemSimplifiedResponse;
import club.pard.exit.actionitem.entity.ActionItem;
import club.pard.exit.user.entity.User;
import club.pard.exit.actionitem.repo.ActionItemRepo;
import club.pard.exit.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionItemService {
    private final ActionItemRepo actionItemRepo;
    private final UserRepo userRepo;

    @Transactional
    public ResponseEntity<Response<ActionItemSimplifiedResponse>> add(Long userId, AddActionItemRequest request)
    {
        try
        {
            User targetUser = userRepo.findById(userId).orElse(null);
            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "ActionItem/add: Target User not existent"));

            String itemCategory = request.getCategory();
            String item = request.getContent();

            if(itemCategory == null || itemCategory.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("Action Item의 카테고리가 존재하지 않거나 비어있어요!", "ActionItem/add: Action Item has category null or empty"));
            if(item == null || item.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("Action Item의 내용이 비어있어요!", "ActionItem/add: Action Item has name null or empty"));
            if(actionItemRepo.existsByUserIdAndCategoryAndContent(userId, itemCategory, item))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("같은 이름의 Action Item이 이미 있어요!", "ActionItem/add: Action item already exists with the same name and category"));

            if(targetUser.getActionItems().size() >= 5)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("사용자의 Action Item 갯수 제한(5개)을 이미 채웠어요!", "ActionItem/add: Target User has already reached Action Item number of 5"));


            ActionItem newActionItem = ActionItem.builder()
                    .category(itemCategory)
                    .content(item)
                    .build();
            targetUser.addActionItem(newActionItem);
            actionItemRepo.save(newActionItem);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("Action Item 추가 완료!", "ActionItem/add: Successful", ActionItemSimplifiedResponse.from(newActionItem)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "ActionItem/add: Internal Server Error"));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Response<List<ActionItemSimplifiedResponse>>> list(Long userId)
    {
        try
        {
            User targetUser = userRepo.findById(userId).orElse(null);
            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("사용자가 존재하지 않아요!", "ActionItem/list: Target User does not exist"));

            List<ActionItem> items = actionItemRepo.findAllByUserIdOrderByIdAsc(userId);
            if(items == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("Action Item 리스트가 존재하지 않아요!", "ActionItem/list: Action Item list is null"));

            List<ActionItemSimplifiedResponse> itemsSimplified = new ArrayList<>();
            items.forEach((item) -> {
                itemsSimplified.add(ActionItemSimplifiedResponse.from(item));
            });

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("Action Item 목록 조회 완료!", "ActionItem/list: Successful", itemsSimplified));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "ActionItem/list: Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Response<ActionItemSimplifiedResponse>> expose(Long userId)
    {
        try
        {
            if(!userRepo.existsById(userId))
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("사용자가 존재하지 않아요!", "ActionItem/expose: Target User does not exist"));

            ActionItem targetActionItem = actionItemRepo.findFirst1ByUserIdOrderByExposureCountAsc(userId);
            if(targetActionItem == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("Action Item이 존재하지 않아요!", "ActionItem/expose: Action Item is null")); // Expected NOT to be stuck here when the list is empty(not null)

            targetActionItem.setExposureCount(targetActionItem.getExposureCount() + 1);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("Action Item 가져오기 완료!", "ActionItem/expose: successful", ActionItemSimplifiedResponse.from(targetActionItem)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "ActionItem/expose: Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Response<ActionItemSimplifiedResponse>> update(Long userId, Long itemId, UpdateActionItemRequest request)
    {
        try
        {
            User targetUser = userRepo.findById(userId).orElse(null);
            ActionItem targetActionItem = actionItemRepo.findById(itemId).orElse(null);

            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "ActionItem/update: Target User not existent"));
            if(targetActionItem == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 Action Item이 존재하지 않아요!", "ActionItem/update: Target Action Item not existent"));
            if(targetActionItem.getUser().getId() != userId)
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Response.setFailure("해당 사용자가 해당 Action Item을 소유하지 않아요!", "ActionItem/update: Target Action Item not owned by target User"));


            String newCategory = request.getCategory();
            String newContent = request.getContent();

            if(actionItemRepo.existsByUserIdAndCategoryAndContent(userId, newCategory, newContent))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.setFailure("같은 카테고리에 같은 이름으로 되어 있는 Action Item이 이미 있어요!", "ActionItem/update: Action Item already existent with same category and content"));

            targetActionItem.setCategory(newCategory);
            targetActionItem.setContent(newContent);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("Action Item 수정 완료!", "ActionItem/update: successful", ActionItemSimplifiedResponse.from(targetActionItem)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "ActionItem/update: Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Response<?>> remove(Long userId, Long itemId)
    {
        try
        {
            User targetUser = userRepo.findById(userId).orElse(null);
            ActionItem targetActionItem = actionItemRepo.findById(itemId).orElse(null);

            if(targetUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 사용자가 존재하지 않아요!", "ActionItem/remove: Target User not existent"));
            if(targetActionItem == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.setFailure("해당 Action Item이 존재하지 않아요!", "ActionItem/remove: Target Action Item not existent"));
            if(targetActionItem.getUser().getId() != targetUser.getId())
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Response.setFailure("해당 사용자가 해당 Action Item을 소유하지 않아요!", "ActionItem/remove: Target Action Item not owned by target User"));

            targetUser.removeActionItem(targetActionItem);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.setSuccess("Action Item 삭제 완료!", "ActionItem/remove: successful", null));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.setFailure("서버 내부에 오류가 생겼어요!", "ActionItem/remove: Internal Server Error"));
        }
    }
}

