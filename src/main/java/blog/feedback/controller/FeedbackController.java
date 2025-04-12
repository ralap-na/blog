package blog.feedback.controller;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/{userId}/{articleId}/comment-edited")
    public ResponseEntity<String> createComment(@PathVariable String userId, @PathVariable String articleId, @RequestBody String content){
        OperationOutcome outcome = feedbackService.createComment(articleId, userId, content, Instant.now());

        if(outcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{userId}/{articleId}/{commentId}/comment-edited")
    public ResponseEntity<String> updateComment(@PathVariable String userId, @PathVariable String articleId, @PathVariable String commentId, @RequestBody String content){
        OperationOutcome outcome = feedbackService.updateComment(commentId, userId, content, Instant.now());

        if(outcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{userId}/{articleId}/add-reaction")
    public ResponseEntity<String> addReaction(@PathVariable String userId, @PathVariable String articleId, @RequestBody String type){
        OperationOutcome outcome = feedbackService.addReaction(articleId, userId, type);

        if(outcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{userId}/{articleId}/{reactionId}/delete-reaction")
    public ResponseEntity<String> deleteReaction(@PathVariable String userId, @PathVariable String articleId, @PathVariable String reactionId){
        OperationOutcome outcome = feedbackService.removeReaction(reactionId, userId);

        if(outcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }
}
