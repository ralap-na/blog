package blog.feedback;

public class Reaction {
    private final String id;
    private String userId;
    private String articleId;
    private final String commentId;
    private final String type;

    public Reaction(String id, String userId, String articleId, String type) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.type = type;
        commentId = null;
    }

    public Reaction(String id, String userId, String articleId, String commentId, String type) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.commentId = commentId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getType() {
        return type;
    }
}
