package blog.article.service;

import blog.article.Article;
import blog.article.Repository;
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

    public String create(String userId, String articleId, String title, String content, String tag, String category, Instant date) {
        Article article = new Article(userId, articleId, title, content, tag, category, date, false);

        repository.saveArticle(article);

        article = repository.findArticleById(articleId);

        if(article == null){
            return null;
        }

        return  articleId;

    }

    public Collection<Article> getArticlesByUserId(String userId){

        return repository.findArticlesByUserId(userId);
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

    public boolean delete(String userId, String articleId){
        Article article = repository.findArticleById(articleId);

        if (article == null) {
            return false; // 文章不存在，返回 false
        }

        if(userId.equals(article.getUserId())){
            repository.delete(articleId);
        }

        if(repository.findArticleById(articleId) == null && repository.findDeletedArticleById(articleId) != null){
            return true;
        }

        return false;
    }

    public Collection<Article> getAllDeletedArticlesByUserId(String userId) {
        Collection<Article> articles = repository.findAllDeletedArticlesByUserId(userId);
        if(articles == null){
            return null;
        }

        return articles;
    }

    public boolean recover(String userId, String articleId){
        Article article = repository.findDeletedArticleById(articleId);

        if (article == null) {
            return false; // 文章不存在，返回 false
        }

        if(userId.equals(article.getUserId())){
            repository.recover(articleId);
        }

        if(repository.findArticleById(articleId) != null && repository.findDeletedArticleById(articleId) == null){
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
        List<Collection<Article>> filters = new ArrayList<>();

        if (tag != null && !tag.isEmpty()) {
            Collection<Article> tagArticles = repository.findArticlesByTag(tag);
            if (tagArticles == null) return null;
            filters.add(tagArticles);
        }

        if (category != null && !category.isEmpty()) {
            Collection<Article> categoryArticles = repository.findArticlesByCategory(category);
            if (categoryArticles == null) return null;
            filters.add(categoryArticles);
        }

        if (title != null && !title.isEmpty()) {
            Collection<Article> titleArticles = repository.findArticlesByTitle(title);
            if (titleArticles == null) return null;
            filters.add(titleArticles);
        }

        if (filters.isEmpty()) {
            return null;
        }

        Collection<Article> result = new ArrayList<>(filters.get(0));
        for (int i = 1; i < filters.size(); i++) {
            result.retainAll(filters.get(i));
        }

        return result;
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