package blog.article.service;

import blog.article.Article;
import blog.article.Repository;
import blog.user.User;
import blog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private Repository repository;

    @Autowired
    private UserService userService;

    public String create(String userId, String articleId, String title, String content, String tag, String category, Instant date) {
        Article article = new Article(userId, articleId, title, content, tag, category, date, false);
        User user = userService.getUser(userId);

        articleId = user.addArticle(article);
        repository.saveUser(user);

        article = user.findArticleById(articleId);

        if(article == null){
            return null;
        }

        return  articleId;

    }

    public Collection<Article> getArticlesByUserId(String userId){
        User user = userService.getUser(userId);

        return user.getArticleList();
    }

    public Article getArticle(String articleId) {
        Article article = repository.findArticleById(articleId);
        if(article == null){
            return null;
        }

        return article;
    }

    public Boolean update(String userId, String articleId, String title, String content, String tag, String category){
        User user = repository.findUserById(userId);
        Article article = user.updateArticle(articleId, title, content, tag, category);

        if (article == null) {
            return false; // 文章不存在，返回 false
        }
        repository.saveUser(user);
        return true;
    }

    public boolean delete(String userId, String articleId){
        User user = userService.getUser(userId);
        Article article = user.findArticleById(articleId);

        if (article == null) {
            return false; // 文章不存在，返回 false
        }

        if(userId.equals(article.getUserId())){
            user.deleteArticle(articleId);
            repository.saveUser(user);
        }

        if(user.findArticleById(articleId) == null && user.findDeletedArticleById(articleId) != null){
            return true;
        }

        return false;
    }

    public Collection<Article> getAllDeletedArticlesByUserId(String userId) {
        User user = userService.getUser(userId);
        Collection<Article> articles = user.getDeletedArticleList();
        if(articles == null){
            return null;
        }

        return articles;
    }

    public boolean recover(String userId, String articleId){
        User user = userService.getUser(userId);
        Article article = user.findDeletedArticleById(articleId);

        if (article == null) {
            return false; // 文章不存在，返回 false
        }

        if(userId.equals(article.getUserId())){
            user.recoverArticle(articleId);
            repository.saveUser(user);
        }

        if(user.findArticleById(articleId) != null && user.findDeletedArticleById(articleId) == null){
            return true;
        }

        return false;
    }

    public Collection<Article> getAllArticles() {
        Collection<Article> articles = repository.findAllArticles();
        if(articles == null){
            return null;
        }

        return articles;
    }

    public Collection<Article> getArticlesByConditions(String title, String category, String tag) {
        Collection<Article> articles = repository.findAllArticles();

        List<Collection<Article>> filters = new ArrayList<>();

        if (tag != null && !tag.isEmpty()) {
            articles = articles.stream().filter(article -> article.getTag().contains(tag)).toList();

            if (articles == null) return null;

        }

        if (category != null && !category.isEmpty()) {
            articles = articles.stream().filter(article -> article.getCategory().contains(category)).toList();

            if (articles == null) return null;
        }

        if (title != null && !title.isEmpty()) {
            articles = articles.stream().filter(article -> article.getTitle().contains(title)).toList();

            if (articles == null) return null;
        }

        if (articles.isEmpty()) {
            return null;
        }

        return articles;
    }


    public Collection<Article> getArticlesByTag(String tag) {
        Collection<Article> articles = repository.findArticlesByTag(tag);
        if(articles == null){
            return null;
        }

        return articles;
    }

    public Collection<Article> getArticlesByCategory(String category) {
        Collection<Article> articles = repository.findArticlesByCategory(category);
        if(articles == null){
            return null;
        }

        return articles;
    }

    public Collection<Article> getArticlesByTitle(String keyword){
        Collection<Article> articles = repository.findArticlesByTitle(keyword);

        if(articles == null){
            return null;
        }

        return articles;
    }
}