package blog.article;

import java.util.ArrayList;
import java.util.List;

public class ArticleCollection {
    private String collectionId;
    private String userId;
    private List<String> articleIds;

    public ArticleCollection(){}

    public ArticleCollection(String collectionId, String userId) {
        this.collectionId = collectionId;
        this.userId = userId;
        this.articleIds = new ArrayList<>();
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addArticle(String articleId) {
        articleIds.add(articleId);
    }

    public void deleteArticle(String articleId) {
        articleIds.remove(articleId);
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getArticleIds() {
        return articleIds;
    }
}
