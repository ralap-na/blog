package blog.article.service;

import blog.article.Article;
import blog.article.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ArticleService {

    @Autowired
    private Repository repository;

    public String create(String userId, String articleId, String title, String content, String tag, String category, Instant date) {
        Article article = new Article(userId, articleId, title, content, tag, category, date, false);

        repository.saveArticle(article);

        article = repository.findArticleById(articleId);

        if(article == null){
            return null;
        }

        return  articleId;

    }

    public Article getArticle(String articleId) {
        Article article = repository.findArticleById(articleId);
        if(article == null){
            return null;
        }

        return article;
    }

    public Boolean update(String articleId, String title, String content, String tag, String category){
        Article article = repository.findArticleById(articleId);

        if (article == null) {
            return false; // 文章不存在，返回 false
        }
        article.update(title, content, tag, category);
        repository.saveArticle(article);
        return true;
    }
}
