package blog.user;

import blog.article.Article;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void updateUser() {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        user.update("new username", "new password");

        assertEquals("new username", user.getUsername());
        assertEquals("new password", user.getPassword());

    }

    @Test
    public void addArticle(){
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        Article article = buildTestArticle(userId, "1", "Add Article", false);

        user.addArticle(article);

        Article savedArticle = user.findArticleById("1");

        assertEquals("Add Article Title", savedArticle.getTitle());
        assertEquals("Add Article Content", savedArticle.getContent());
        assertEquals("Add Article Tag", savedArticle.getTag());
        assertEquals("Add Article Category", savedArticle.getCategory());
    }

    @Test
    public void updateArticle(){
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        Article article = buildTestArticle(userId, "1", "Add Article", false);

        user.addArticle(article);
        user.updateArticle("1", "Update Article Title", "Update Article Content", "Update Article Tag", "Update Article Category");

        Article updatedArticle = user.findArticleById("1");

        assertEquals("Update Article Title", updatedArticle.getTitle());
        assertEquals("Update Article Content", updatedArticle.getContent());
        assertEquals("Update Article Tag", updatedArticle.getTag());
        assertEquals("Update Article Category", updatedArticle.getCategory());
    }

    @Test
    public void findArticleById(){
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        Article article = buildTestArticle(userId, "1", "Find Article 1", false);
        Article article2 = buildTestArticle(userId, "2", "Find Article 2", false);

        user.addArticle(article);
        user.addArticle(article2);

        Article expected = user.findArticleById("1");

        assertEquals("Find Article 1 Title", expected.getTitle());
        assertEquals("Find Article 1 Content", expected.getContent());
        assertEquals("Find Article 1 Tag", expected.getTag());
        assertEquals("Find Article 1 Category", expected.getCategory());
    }

    @Test
    public void findDeletedArticleById(){
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        Article article = buildTestArticle(userId, "1", "Find DeletedArticle 1", false);
        Article article2 = buildTestArticle(userId, "2", "Find DeletedArticle 2", false);

        user.addArticle(article);
        user.addArticle(article2);

        user.deleteArticle("1");
        user.deleteArticle("2");

        Article expected = user.findDeletedArticleById("1");

        assertEquals("Find DeletedArticle 1 Title", expected.getTitle());
        assertEquals("Find DeletedArticle 1 Content", expected.getContent());
        assertEquals("Find DeletedArticle 1 Tag", expected.getTag());
        assertEquals("Find DeletedArticle 1 Category", expected.getCategory());
    }

    @Test
    public void deleteArticle(){
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        Article article = buildTestArticle(userId, "1", "Find DeletedArticle 1", false);

        user.addArticle(article);

        boolean success = user.deleteArticle("1");

        assertTrue(success);
        assertFalse(user.getDeletedArticleList().isEmpty());
    }

    @Test
    public void recoverArticle(){
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        Article article = buildTestArticle(userId, "1", "Find DeletedArticle 1", false);

        user.addArticle(article);

        boolean success = user.deleteArticle("1");
        assertTrue(success);
        assertFalse(user.getDeletedArticleList().isEmpty());

        success = user.recoverArticle("1");
        assertTrue(success);
        assertFalse(user.getArticleList().isEmpty());
    }

    private Article buildTestArticle(String userId, String articleId, String action, boolean deleted) {
        Instant fixedTime = Instant.parse("2025-06-07T00:00:00Z");
        Article article = new Article();
        article.setUserId(userId);
        article.setArticleId(articleId);
        article.setTitle(action + " Title");
        article.setContent(action + " Content");
        article.setTag(action + " Tag");
        article.setCategory(action + " Category");
        article.setDate(fixedTime);
        article.setDeleted(deleted);
        return article;
    }
}
