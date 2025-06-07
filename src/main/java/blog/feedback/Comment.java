package blog.feedback;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private String id;
    private String articleId;
    private String userId;
    private String content;
    private Instant date;
    private List<Reaction> reactionList;

    public Comment(String id, String articleId, String userId, String content, Instant date) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.content = content;
        this.date = date;
        reactionList = new ArrayList<>();
    }

    public void update(String content, Instant date) {
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getDate() {
        return date;
    }

    public void addReaction(Reaction reaction) {
        reactionList.add(reaction);
    }

    public void removeReaction(Reaction reaction) {
        reactionList.remove(reaction);
    }

    public boolean hasReaction(String reactionId) {
        return reactionList.stream().anyMatch(reaction -> reaction.getId().equals(reactionId));
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
