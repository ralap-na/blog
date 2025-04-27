package blog.article.controller;

import blog.article.Article;
import blog.article.service.BookmarkService;
import blog.article.service.ArticleService;
import blog.article.service.CategoryService;
import blog.user.User;
import blog.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;

    @PostMapping("/")
    public ResponseEntity<String> createArticle(@RequestBody String info){
        JSONObject jsonObject = new JSONObject(info);
        String articleId = UUID.randomUUID().toString();
        String userId = jsonObject.getString("userId");
        String title = jsonObject.getString("title");
        String content = jsonObject.getString("content");
        String tag = jsonObject.getString("tag");
        String category = jsonObject.getString("category");
        Instant date = Instant.parse(jsonObject.getString("date"));

        if(title.isEmpty() || category.isEmpty() || content.isEmpty()){
            return ResponseEntity.internalServerError().body("Title, Category, and Content cannot Empty.");
        }

        articleId = articleService.create(userId, articleId, title, content, tag, category, date);

        if(articleId != null){
            return ResponseEntity.ok().body(articleId);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Collection<Article>> getArticlesByUserId(@PathVariable String userId){
        Collection<Article> articles = articleService.getArticlesByUserId(userId);

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<Article> getArticle(@PathVariable String articleId){
        Article article = articleService.getArticle(articleId);

        if(article != null){
            return ResponseEntity.ok().body(article);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<String> updateArticle(@RequestBody String info, @PathVariable String articleId){

        JSONObject jsonObject = new JSONObject(info);

        String title = jsonObject.getString("title");
        String content = jsonObject.getString("content");
        String tag = jsonObject.getString("tag");
        String category = jsonObject.getString("category");

        if(title.isEmpty() || category.isEmpty() || content.isEmpty()){
            return ResponseEntity.internalServerError().body("Title, Category, and Content cannot Empty.");
        }

        Boolean message = articleService.update(articleId, title, content, tag, category);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{userId}/{articleId}")
    public ResponseEntity<String> deleteArticle(@PathVariable(value="userId") String userId, @PathVariable(value="articleId") String articleId){
        boolean message = articleService.delete(userId, articleId);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/all/deleted/{userId}")
    public ResponseEntity<Collection<Article>> getAllDeletedArticlesByUserId(@PathVariable String userId){
        Collection<Article> articles = articleService.getAllDeletedArticlesByUserId(userId);

        if(articles != null){
            return ResponseEntity.ok().body(articles);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{userId}/{articleId}")
    public ResponseEntity<String> recoverArticle(@PathVariable(value="userId") String userId, @PathVariable(value="articleId") String articleId){
        boolean message = articleService.recover(userId, articleId);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Article>> getAllArticles(){
        Collection<Article> articles = articleService.getAllArticles();

        if(articles != null){
            return ResponseEntity.ok().body(articles);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/condition")
    public ResponseEntity<Collection<Article>> getArticlesByConditions(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag) {

        try {
            if (tag != null) {
                tag = URLDecoder.decode(tag, StandardCharsets.UTF_8.name());
            }
            if (category != null) {
                category = URLDecoder.decode(category, StandardCharsets.UTF_8.name());
            }
            if (title != null) {
                title = URLDecoder.decode(title, StandardCharsets.UTF_8.name());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Collection<Article> articles = articleService.getArticlesByConditions(title, category, tag);

        if (articles == null || articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<Collection<Article>> getArticlesByTag(@PathVariable String tag){
        Collection<Article> articles = articleService.getArticlesByTag(tag);

        if(articles != null){
            return ResponseEntity.ok().body(articles);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Collection<Article>> getArticlesByCategory(@PathVariable String category){
        Collection<Article> articles = articleService.getArticlesByCategory(category);

        if(articles != null){
            return ResponseEntity.ok().body(articles);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/title/{keyword}")
    public ResponseEntity<Collection<Article>> getArticlesByTitle(@PathVariable String keyword){
        Collection<Article> articles = articleService.getArticlesByTitle(keyword);

        return ResponseEntity.ok().body(articles);
    }

    @PutMapping("/bookmark/{userId}/{articleId}")
    public ResponseEntity<String> addToBookmark(@PathVariable(value="userId") String userId, @PathVariable(value="articleId") String articleId){
        boolean message = bookmarkService.addArticle(userId, articleId);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/bookmark/{userId}/{articleId}")
    public ResponseEntity<String> deleteFromBookmark(@PathVariable(value="userId") String userId, @PathVariable(value="articleId") String articleId){
        boolean message = bookmarkService.deleteArticle(userId, articleId);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/bookmark/{userId}")
    public ResponseEntity<List<String>> getArticleIdsFromBookmarkByUserId(@PathVariable String userId){
        List<String> articleIds = bookmarkService.getArticleIds(userId);

        if(articleIds != null){
            return ResponseEntity.ok().body(articleIds);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/category/create")
    public ResponseEntity<String> createCategory(@RequestParam String name, HttpSession session) {

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in.");
        }

        User user = userService.getUser(userId);

        if(Objects.equals(user.getUsername(), "Admin")){
            String response = categoryService.createCategory(name);
            if (response.startsWith("Success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Admin can create category.");
        }
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id, HttpSession session) {

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in.");
        }

        User user = userService.getUser(userId);

        if(Objects.equals(user.getUsername(), "Admin")){
            String response = categoryService.deleteCategory(id);
            if (response.startsWith("Success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Admin can delete category.");
        }
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getAllCategories(HttpSession session) {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}