package blog.article.service;

import blog.article.Article;
import blog.article.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired
    private Repository repository;

    public Boolean update(String articleId, String title, String content, String tag, String category){
        Article article = repository.findArticleById(articleId);

        article.update(title, content, tag, category);

        repository.saveArticle(article);
        return true;
    }
}
