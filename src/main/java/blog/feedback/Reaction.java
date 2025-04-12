package blog.feedback;

public class Reaction {
    private String id;
    private String userId;
    private String articleId;
    private String type;

    public Reaction(String id, String userId, String articleId, String type) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
