package blog.article;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Repository {

    private Map<String, Article> articleList = new HashMap<>();
    private Map<String, Article> deletedArticleList = new HashMap<>();

    public Article findArticleById(String articleId){
        return articleList.get(articleId);
    }

    public void saveArticle(Article article) {
        articleList.put(article.getArticleId(), article);
    }

    public Article findDeletedArticleById(String articleId){
        return deletedArticleList.get(articleId);
    }

    public void delete(String articleId) {
        Article article = articleList.get(articleId);
        article.delete();
        articleList.remove(articleId);

        deletedArticleList.put(articleId, article);
    }

    public void recover(String articleId){
        Article article = deletedArticleList.get(articleId);
        article.recover();
        deletedArticleList.remove(articleId);

        articleList.put(articleId, article);
    }
}
