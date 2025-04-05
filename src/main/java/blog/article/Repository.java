package blog.article;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Repository {

    private Map<String, Article> articleList = new HashMap<>();

    public Article findArticleById(String articleId){
        return articleList.get(articleId);
    }

    public void saveArticle(Article article) {
        articleList.put(article.getArticleId(), article);
    }
}
