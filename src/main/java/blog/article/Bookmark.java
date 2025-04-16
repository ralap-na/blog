package blog.article;

import java.util.ArrayList;
import java.util.List;

public class Bookmark {
    private String userId;
    private List<String> articleIds;

    public Bookmark(){}

    public Bookmark(String userId) {
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
}