package blog.feedback.controller;

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
        Boolean message = feedbackService.createComment(articleId, userId, content, Instant.now());

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{userId}/{articleId}/{commentId}/comment-edited")
    public ResponseEntity<String> updateComment(@PathVariable String userId, @PathVariable String articleId, @PathVariable String commentId, @RequestBody String content){
        Boolean message = feedbackService.updateComment(commentId, userId, content, Instant.now());

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{userId}/{articleId}/add-reaction")
    public ResponseEntity<String> addReaction(@PathVariable String userId, @PathVariable String articleId, @RequestBody String type){
        Boolean message = feedbackService.addReaction(articleId, userId, type);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{userId}/{articleId}/{reactionId}/delete-reaction")
    public ResponseEntity<String> deleteReaction(@PathVariable String userId, @PathVariable String articleId, @PathVariable String reactionId){
        Boolean message = feedbackService.removeReaction(reactionId, userId);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }
}
