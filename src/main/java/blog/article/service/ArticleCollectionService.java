package blog.article.service;

import blog.article.ArticleCollection;
import blog.article.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ArticleCollectionService {
    @Autowired
    private Repository repository;

    public boolean addArticle(String userId, String articleId) {
        ArticleCollection collection = repository.findCollectionByUserId(userId);

        if (collection == null) {
            String collectionId = UUID.randomUUID().toString();
            collection = new ArticleCollection(collectionId, userId); // first time add article
            repository.saveCollection(collection);
        }

        collection.addArticle(articleId);
        return true;
    }

    public boolean deleteArticle(String userId, String articleId) {
        ArticleCollection collection = repository.findCollectionByUserId(userId);

        if (collection == null) {
            return false;
        }

        collection.deleteArticle(articleId);
        return true;
    }
}
