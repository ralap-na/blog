package blog.article;

import java.util.ArrayList;
import java.util.List;

public class Bookmark {
    private String bookmarkId;
    private String bookmarkName;
    private String userId;
    private List<String> articleIds;

    public Bookmark(){}

    public Bookmark(String bookmarkId, String bookmarkName, String userId) {
        this.bookmarkId = bookmarkId;
        this.bookmarkName = bookmarkName;
        this.userId = userId;
        this.articleIds = new ArrayList<>();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
    }

    public void addArticle(String articleId) {
        articleIds.add(articleId);
    }

    public void deleteArticle(String articleId) {
        articleIds.remove(articleId);
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getArticleIds() {
        return articleIds;
    }

    public String getBookmarkId() {
        return bookmarkId;
    }

    public String getBookmarkName() {
        return bookmarkName;
    }
}