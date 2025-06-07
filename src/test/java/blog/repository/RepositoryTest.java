package blog.repository;

import blog.article.Article;
import blog.article.Bookmark;
import blog.article.Category;
import blog.article.Repository;
import blog.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class RepositoryTest {

    private Repository repository;

    @BeforeEach
    public void setUp() {
        repository = new Repository();
        repository.initDefaultCategories(); // 模擬 @PostConstruct 行為
    }

    @Test
    public void findAllArticles(){
        Article article = buildTestArticle("Admin", "1", "FindAll", false);
        Article article2 = buildTestArticle("Other", "2", "FindAll", false);

        User user = new User("Admin", "Admin", "Admin");
        user.addArticle(article);
        repository.saveUser(user);

        User user2 = new User("Other", "Other", "Other");
        user2.addArticle(article2);
        repository.saveUser(user2);

        Collection<Article> articles = repository.findAllArticles();

        assertEquals(2, articles.size());
    }

    @Test
    public void findAllDeletedArticlesByUserId(){
        Article article = buildTestArticle("Admin", "1", "FindAllDeleted", false);
        Article article2 = buildTestArticle("Other", "2", "FindAllDeleted", false);

        User user = new User("Admin", "Admin", "Admin");
        user.addArticle(article);
        repository.saveUser(user);

        User user2 = new User("Other", "Other", "Other");
        user2.addArticle(article2);
        repository.saveUser(user2);

        user.deleteArticle(article.getArticleId());
        user2.deleteArticle(article2.getArticleId());

        Collection<Article> articles = repository.findAllDeletedArticlesByUserId("Admin");

        assertEquals(1, articles.size());

        for(Article a : articles){
            assertTrue(a.getDeleted());
            assertEquals("Admin", a.getUserId());
        }
    }

    @Test
    public void findArticlesByTitle(){
        Article article = buildTestArticle("Admin", "1", "FindArticlesByTitleA", false);
        Article article2 = buildTestArticle("Other", "2", "FindArticlesByTitleB", false);

        User user = new User("Admin", "Admin", "Admin");
        user.addArticle(article);
        repository.saveUser(user);

        User user2 = new User("Other", "Other", "Other");
        user2.addArticle(article2);
        repository.saveUser(user2);

        Collection<Article> articles = repository.findArticlesByTitle("FindArticlesByTitleB");

        assertFalse(articles.isEmpty());
        for(Article a : articles){
            assertEquals("FindArticlesByTitleB Title", a.getTitle());
        }
    }

    @Test
    public void findArticlesByTag(){
        Article article = buildTestArticle("Admin", "1", "FindArticlesByTagA", false);
        Article article2 = buildTestArticle("Other", "2", "FindArticlesByTagB", false);

        User user = new User("Admin", "Admin", "Admin");
        user.addArticle(article);
        repository.saveUser(user);

        User user2 = new User("Other", "Other", "Other");
        user2.addArticle(article2);
        repository.saveUser(user2);

        Collection<Article> articles = repository.findArticlesByTag("FindArticlesByTagB");

        assertFalse(articles.isEmpty());
        for(Article a : articles){
            assertEquals("FindArticlesByTagB Tag", a.getTag());
        }
    }

    @Test
    public void findArticlesByCategory(){
        Article article = buildTestArticle("Admin", "1", "FindArticlesByCategoryA", false);
        Article article2 = buildTestArticle("Other", "2", "FindArticlesByCategoryB", false);

        User user = new User("Admin", "Admin", "Admin");
        user.addArticle(article);
        repository.saveUser(user);

        User user2 = new User("Other", "Other", "Other");
        user2.addArticle(article2);
        repository.saveUser(user2);

        Collection<Article> articles = repository.findArticlesByCategory("FindArticlesByCategoryB");

        assertFalse(articles.isEmpty());
        for(Article a : articles){
            assertEquals("FindArticlesByCategoryB Category", a.getCategory());
        }
    }

    @Test
    public void findArticlesByUserIds(){
        Article article = buildTestArticle("Admin", "1", "FindArticlesByCategoryA", false);
        Article article2 = buildTestArticle("Other", "2", "FindArticlesByCategoryB", false);

        User user = new User("Admin", "Admin", "Admin");
        user.addArticle(article);
        repository.saveUser(user);

        User user2 = new User("Other", "Other", "Other");
        user2.addArticle(article2);
        repository.saveUser(user2);

        Collection<Article> articles = repository.findArticlesByUserId("Admin");

        assertFalse(articles.isEmpty());
        for(Article a : articles){
            assertEquals("Admin", a.getUserId());
        }
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

    @Test
    public void saveBookmark() {
        String userId = "u1";
        String bookmarkId = "b1";
        String bookmarkName = "Bookmark-1";
        Bookmark bookmark = new Bookmark(bookmarkId, bookmarkName, userId);

        repository.saveBookmark(bookmark);

        Bookmark bookmark1 = repository.findBookmarkByBookmarkId(bookmarkId);

        assertEquals(userId, bookmark1.getUserId());
    }

    @Test
    public void findBookmarksByUserId() {
        String userId = "u1";
        String bookmarkId_1 = "b1";
        String bookmarkName_1 = "Bookmark-1";
        String bookmarkId_2 = "b2";
        String bookmarkName_2 = "Bookmark-2";
        Bookmark bookmark_1 = new Bookmark(bookmarkId_1, bookmarkName_1, userId);
        Bookmark bookmark_2 = new Bookmark(bookmarkId_2, bookmarkName_2, userId);
        repository.saveBookmark(bookmark_1);
        repository.saveBookmark(bookmark_2);

        List<Bookmark> bookmarkList = repository.findBookmarksByUserId(userId);

        assertTrue(bookmarkList.contains(bookmark_1));
        assertTrue(bookmarkList.contains(bookmark_2));
        assertEquals(2, bookmarkList.size());
    }

    @Test
    public void shouldInitializeDefaultCategories() {
        Collection<Category> categories = repository.findAllCategories();

        assertEquals(2, categories.size()); // 根據 DefaultCategory 有 5 個
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("TECH")));
    }

    @Test
    public void saveCategory() {
        String id = UUID.randomUUID().toString();
        Category category = new Category(id, "TestCategory");

        repository.saveCategory(category);

        Category saved = repository.findCategoryById(id);
        assertNotNull(saved);
        assertEquals("TestCategory", saved.getName());
    }

    @Test
    public void findAllCategories() {
        int initialSize = repository.findAllCategories().size();

        Category category1 = new Category(UUID.randomUUID().toString(), "X");
        Category category2 = new Category(UUID.randomUUID().toString(), "Y");

        repository.saveCategory(category1);
        repository.saveCategory(category2);

        Collection<Category> all = repository.findAllCategories();

        assertEquals(initialSize + 2, all.size());
    }

    @Test
    public void deleteCategory() {
        String id = UUID.randomUUID().toString();
        Category category = new Category(id, "DeleteMe");

        repository.saveCategory(category);
        assertNotNull(repository.findCategoryById(id));

        repository.deleteCategoryById(id);
        assertNull(repository.findCategoryById(id));
    }

    @Test
    public void deleteNonExistentCategory() {
        String fakeId = UUID.randomUUID().toString();
        repository.deleteCategoryById(fakeId); // 不會丟錯誤，但不影響 map

        assertNull(repository.findCategoryById(fakeId));
    }
}
