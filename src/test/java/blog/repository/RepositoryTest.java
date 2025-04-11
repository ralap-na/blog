package blog.repository;

import blog.article.Article;
import blog.article.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RepositoryTest {

    private final Repository repository = new Repository();

    @Test
    public void saveArticle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Saved Title");
        article.setContent("Saved Content");
        article.setTag("Saved Tag");
        article.setCategory("Saved Category");

        repository.saveArticle(article);

        Article saveedArticle = repository.findArticleById("1");

        assertEquals("Saved Title", saveedArticle.getTitle());
        assertEquals("Saved Content", saveedArticle.getContent());
        assertEquals("Saved Tag", saveedArticle.getTag());
        assertEquals("Saved Category", saveedArticle.getCategory());
    }

    @Test
    public void deleteArticle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Deleted Title");
        article.setContent("Deleted Content");
        article.setTag("Deleted Tag");
        article.setCategory("Deleted Category");

        repository.saveArticle(article);

        repository.delete("1");

        Article deletedArticle = repository.findDeletedArticleById("1");
        assertEquals("Deleted Title", deletedArticle.getTitle());
        assertEquals("Deleted Content", deletedArticle.getContent());
        assertEquals("Deleted Tag", deletedArticle.getTag());
        assertEquals("Deleted Category", deletedArticle.getCategory());
        assertEquals(true, deletedArticle.getDeleted());
    }

    @Test
    public void recoverArticle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Recovered Title");
        article.setContent("Recovered Content");
        article.setTag("Recovered Tag");
        article.setCategory("Recovered Category");

        repository.saveArticle(article);

        repository.delete("1");
        Article deletedArticle = repository.findDeletedArticleById("1");
        assertEquals(true, deletedArticle.getDeleted());

        repository.recover("1");
        Article recoveredArticle = repository.findArticleById("1");
        assertEquals("Recovered Title", deletedArticle.getTitle());
        assertEquals("Recovered Content", deletedArticle.getContent());
        assertEquals("Recovered Tag", deletedArticle.getTag());
        assertEquals("Recovered Category", deletedArticle.getCategory());
        assertEquals(false, recoveredArticle.getDeleted());
    }
}
