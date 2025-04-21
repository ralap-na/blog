package blog.article.service;

import blog.article.Category;
import blog.article.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class CategoryService {

    private final Repository repository;

    @Autowired
    public CategoryService(Repository repository) {
        this.repository = repository;
    }

    public String createCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Error: Category name is required.";
        }

        // 避免新增重複名稱
        boolean exists = repository.findAllCategories().stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase(name));
        if (exists) {
            return "Error: Category name already exists.";
        }

        String id = UUID.randomUUID().toString();
        Category category = new Category(id, name);
        repository.saveCategory(category);
        return "Success: Category created.";
    }

    public String deleteCategory(String id) {
        boolean removed = repository.deleteCategoryById(id);
        if (removed) {
            return "Success: Category deleted.";
        } else {
            return "Error: Category not found.";
        }
    }

    public Collection<Category> getAllCategories() {
        return repository.findAllCategories();
    }
}