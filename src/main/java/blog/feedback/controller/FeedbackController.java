package blog.feedback.controller;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.feedback.Comment;
import blog.feedback.Reaction;
import blog.feedback.service.FeedbackService;
import blog.notification.service.NotificationService;
import jakarta.servlet.http.HttpSession;
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

    // Comment
    @PostMapping("/{writerId}/{articleId}/{readerId}/comment-edited")
    public ResponseEntity<String> createComment(@PathVariable String writerId, @PathVariable String articleId, @PathVariable String readerId,@RequestBody String content){
        OperationOutcome feedbackOutcome = feedbackService.createComment(articleId, readerId, content, Instant.now());
        OperationOutcome notificationOutcome = notificationService.notifyUser(writerId, articleId, "Someone left a comment under your article!", "Someone left a comment under your article!", Instant.now());

        if(feedbackOutcome.getState().equals(OutcomeState.SUCCESS) && notificationOutcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{articleId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String articleId, @PathVariable String commentId, HttpSession session){
        String userId = session.getAttribute("userId").toString();

        if(userId == null){
            return ResponseEntity.badRequest().body("You are not logged in.");
        }

        OperationOutcome outcome = feedbackService.deleteComment(commentId, userId);

        if(outcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else if(outcome.getMessage().equals("Invalid user.")){
            return ResponseEntity.badRequest().body(outcome.getMessage());
        }
        else {
            return ResponseEntity.internalServerError().body(outcome.getMessage());
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

    // Reaction
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
        OperationOutcome notificationOutcome = notificationService.notifyUser(writerId, articleId, "Someone " + type + " your article!", "Someone " + type + " your article!", Instant.now());

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

    @DeleteMapping("/{articleId}/{reactionId}")
    public ResponseEntity<String> deleteReaction(@PathVariable String articleId, @PathVariable String reactionId, HttpSession session){
        String userId = session.getAttribute("userId").toString();

        if(userId == null){
            return ResponseEntity.badRequest().body("You are not logged in.");
        }

        OperationOutcome outcome = feedbackService.removeReaction(reactionId, userId);

        if(outcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else if(outcome.getMessage().equals("Invalid user.")){
            return ResponseEntity.badRequest().body(outcome.getMessage());
        }
        else {
            return ResponseEntity.internalServerError().body(outcome.getMessage());
        }
    }
}
