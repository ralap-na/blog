package blog.feedback.controller;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.feedback.Comment;
import blog.feedback.Reaction;
import blog.feedback.service.FeedbackService;
import blog.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @Autowired
    NotificationService notificationService;

    @PostMapping("/{writerId}/{articleId}/{readerId}/comment-edited")
    public ResponseEntity<String> createComment(@PathVariable String writerId, @PathVariable String articleId, @PathVariable String readerId,@RequestBody String content){
        OperationOutcome feedbackOutcome = feedbackService.createComment(articleId, readerId, content, Instant.now());
        OperationOutcome notificationOutcome = notificationService.notifyUser(writerId, articleId, "You have received a comment!", "Someone left a comment under your article!", Instant.now());

        if(feedbackOutcome.getState().equals(OutcomeState.SUCCESS) && notificationOutcome.getState().equals(OutcomeState.SUCCESS)){
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

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("commentId") String commentId) {
        Comment comment = feedbackService.getCommentById(commentId);

        if(comment != null){
            return ResponseEntity.ok().body(comment);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByArticleId(@PathVariable("articleId") String articleId) {
        List<Comment> comments = feedbackService.getCommentsByArticleId(articleId);

        if(comments != null){
            return ResponseEntity.ok().body(comments);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = feedbackService.getAllComments();

        if(comments != null){
            return ResponseEntity.ok().body(comments);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{articleId}/reactions")
    public ResponseEntity<List<Reaction>> getReactionsByArticleId(@PathVariable("articleId") String articleId) {
        List<Reaction> reactions = feedbackService.getReactionsByArticleId(articleId);

        if(reactions != null){
            return ResponseEntity.ok().body(reactions);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{articleId}/{commentId}/reactions")
    public ResponseEntity<List<Reaction>> getReactionsByCommentId(@PathVariable("articleId") String articleId, @PathVariable("commentId") String commentId) {
        List<Reaction> reactions = feedbackService.getReactionsByCommentId(articleId, commentId);

        if(reactions != null){
            return ResponseEntity.ok().body(reactions);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/reactions")
    public ResponseEntity<List<Reaction>> getAllReactions() {
        List<Reaction> reactions = feedbackService.getAllReactions();

        if(reactions != null){
            return ResponseEntity.ok().body(reactions);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{reactionId}")
    public ResponseEntity<Reaction> getReactionById(@PathVariable("reactionId") String reactionId) {
        Reaction reaction = feedbackService.getReactionById(reactionId);

        if(reaction != null){
            return ResponseEntity.ok().body(reaction);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{writerId}/{articleId}/{readerId}/add-reaction")
    public ResponseEntity<String> addReactionOnArticle(@PathVariable String writerId, @PathVariable String articleId, @PathVariable String readerId, @RequestBody String type){
        OperationOutcome feedbackOutcome = feedbackService.addReactionOnArticle(articleId, readerId, type);
        OperationOutcome notificationOutcome = notificationService.notifyUser(writerId, articleId, "You have received a reaction!", "Someone " + type + " your article!", Instant.now());

        if(feedbackOutcome.getState().equals(OutcomeState.SUCCESS) && notificationOutcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{writerId}/{articleId}/{commentId}/{readerId}/add-reaction")
    public ResponseEntity<String> addReactionOnComment(@PathVariable String writerId, @PathVariable String articleId, @PathVariable String commentId, @PathVariable String readerId, @RequestBody String type){
        OperationOutcome feedbackOutcome = feedbackService.addReactionOnComment(articleId, commentId, readerId, type);
        OperationOutcome notificationOutcome = notificationService.notifyUser(writerId, articleId, "You have received a reaction!", "Someone " + type + " your article!", Instant.now());

        if(feedbackOutcome.getState().equals(OutcomeState.SUCCESS) && notificationOutcome.getState().equals(OutcomeState.SUCCESS)){
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
