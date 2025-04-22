package blog.repository;

import blog.article.Article;
import blog.article.Bookmark;
import blog.article.Category;
import blog.article.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collection;
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
    public void saveArticle(){
        Article article = buildTestArticle("1", "Saved", false);

        repository.saveArticle(article);

        Article savedArticle = repository.findArticleById("1");

        assertEquals("Saved Title", savedArticle.getTitle());
        assertEquals("Saved Content", savedArticle.getContent());
        assertEquals("Saved Tag", savedArticle.getTag());
        assertEquals("Saved Category", savedArticle.getCategory());
    }

    @Test
    public void findAllArticle(){
        Article article = buildTestArticle("1", "A", false);
        Article article2 = buildTestArticle("2", "B", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findAllArticles();

        assertEquals(2, articles.size());
    }

    @Test
    public void findAllDeletedArticlesByUserId(){
        Article article = buildTestArticle("1", "A", false);
        Article article2 = buildTestArticle("2", "B", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);
        repository.delete("1");
        repository.delete("2");

        Collection<Article> articles = repository.findAllDeletedArticlesByUserId("2");

        assertEquals(1, articles.size());

        for(Article a : articles){
            assertTrue(a.getDeleted());
            assertEquals("2", a.getUserId());
        }
    }

    @Test
    public void findArticlesByTag(){
        Article article = buildTestArticle("1", "A", false);
        Article article2 = buildTestArticle("2", "B", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByTag("B");

        for(Article a : articles){
            assertEquals("B Tag", a.getTag());
        }
    }

    @Test
    public void findArticlesByCategory(){
        Article article = buildTestArticle("1", "A", false);
        Article article2 = buildTestArticle("2", "B", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByCategory("B Category");

        for(Article a : articles){
            assertEquals("B Category", a.getCategory());
        }
    }

    @Test
    public void findArticlesByUserIds(){
        Article article = buildTestArticle("1", "Saved", false);

        Article article2 = buildTestArticle("2", "Saved", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByUserId("1");

        for(Article a : articles){
            assertEquals("1", a.getUserId());
        }
    }

    @Test
    public void findArticlesByTitle(){
        Article article = buildTestArticle("1", "Expected", false);

        Article article2 = buildTestArticle("2", "Other", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByTitle("Expected");

        for(Article a : articles){
            assertTrue(a.getTitle().contains("Expected"));
        }
    }

    @Test
    public void deleteArticle(){
        Article article = buildTestArticle("1", "Deleted", false);

        repository.saveArticle(article);

        repository.delete("1");

        Article deletedArticle = repository.findDeletedArticleById("1");
        assertEquals("Deleted Title", deletedArticle.getTitle());
        assertEquals("Deleted Content", deletedArticle.getContent());
        assertEquals("Deleted Tag", deletedArticle.getTag());
        assertEquals("Deleted Category", deletedArticle.getCategory());
        assertTrue(deletedArticle.getDeleted());
    }

    @Test
    public void recoverArticle(){
        Article article = buildTestArticle("1", "Recovered", false);

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
        assertFalse(recoveredArticle.getDeleted());
    }

    private Article buildTestArticle(String id, String action, boolean deleted) {
        Instant fixedTime = Instant.parse("2024-01-01T00:00:00Z");
        Article article = new Article();
        article.setUserId(id);
        article.setArticleId(id);
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
        Bookmark bookmark = new Bookmark(userId);

        repository.saveBookmark(bookmark);

        Bookmark bookmark1 = repository.findBookmarkByUserId(userId);

        assertEquals(userId, bookmark1.getUserId());
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
