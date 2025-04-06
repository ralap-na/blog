package blog.article;

import java.time.Instant;

public class Article {
    private String userId;
    private String articleId;
    private String title;
    private String content;
    private String tag;
    private String category;
    private Instant date;
    private Boolean isDeleted;

    public Article(){}

    public Article(String userId, String articleId, String title, String content, String tag, String category, Instant date, Boolean isDeleted) {
        this.userId = userId;
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.category = category;
        this.date = date;
        this.isDeleted = isDeleted;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getUserId() {
        return userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return tag;
    }

    public String getCategory() {
        return category;
    }

    public Instant getDate() {
        return date;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Article update(String title, String content, String tag, String category){
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.category = category;

        return this;
    }
}
