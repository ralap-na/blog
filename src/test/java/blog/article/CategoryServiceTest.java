package blog.article;

import blog.article.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {

    private CategoryService categoryService;
    private Repository repository;

    @BeforeEach
    public void setUp() {
        repository = new Repository();
        repository.initDefaultCategories(); // 模擬 @PostConstruct
        categoryService = new CategoryService(repository);
    }

    @Test
    public void createCategory_success() {
        String result = categoryService.createCategory("NewCategory");

        assertTrue(result.startsWith("Success"));
        assertEquals(3, repository.findAllCategories().size()); // 預設 2 + 新增 1
    }

    @Test
    public void createCategory_emptyName_shouldFail() {
        String result = categoryService.createCategory("  ");

        assertEquals("Error: Category name is required.", result);
        assertEquals(2, repository.findAllCategories().size()); // 預設 2
    }

    @Test
    public void createCategory_duplicateName_shouldFail() {
        categoryService.createCategory("Tech"); // default 中已有 "TECH"

        String result = categoryService.createCategory("tech"); // 大小寫不同但視為重複

        assertEquals("Error: Category name already exists.", result);
    }

    @Test
    public void deleteCategory_success() {
        // 新增一個分類並取得 ID
        String id = UUID.randomUUID().toString();
        repository.saveCategory(new Category(id, "ToDelete"));

        String result = categoryService.deleteCategory(id);

        assertEquals("Success: Category deleted.", result);
        assertNull(repository.findCategoryById(id));
    }

    @Test
    public void deleteCategory_notFound_shouldFail() {
        String result = categoryService.deleteCategory("non-existent-id");

        assertEquals("Error: Category not found.", result);
    }

    @Test
    public void getAllCategories_shouldReturnAll() {
        repository.saveCategory(new Category(UUID.randomUUID().toString(), "Extra1"));
        repository.saveCategory(new Category(UUID.randomUUID().toString(), "Extra2"));

        Collection<Category> categories = categoryService.getAllCategories();

        assertEquals(4, categories.size()); // 預設 5 + 2 新增
    }
}