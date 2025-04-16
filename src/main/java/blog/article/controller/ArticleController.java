package blog.article.controller;

import blog.article.Article;
import blog.article.service.BookmarkService;
import blog.article.service.ArticleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    BookmarkService bookmarkService;

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody String info){
        JSONObject jsonObject = new JSONObject(info);
        String articleId = UUID.randomUUID().toString();
        String userId = jsonObject.getString("userId");
        String title = jsonObject.getString("title");
        String content = jsonObject.getString("content");
        String tag = jsonObject.getString("tag");
        String category = jsonObject.getString("category");
        Instant date = Instant.parse(jsonObject.getString("date"));

        articleId = articleService.create(userId, articleId, title, content, tag, category, date);

        if(articleId != null){
            return ResponseEntity.ok().body(articleId);
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

    @GetMapping("/all/deleted")
    public ResponseEntity<Collection<Article>> getAllDeletedArticles(){
        Collection<Article> articles = articleService.getAllDeletedArticles();

        if(articles != null){
            return ResponseEntity.ok().body(articles);
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<Collection<Article>> getArticlesByUserId(@PathVariable String userId){
        Collection<Article> articles = articleService.getArticlesByUserId(userId);

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/title/{keyword}")
    public ResponseEntity<Collection<Article>> getArticlesByTitle(@PathVariable String keyword){
        Collection<Article> articles = articleService.getArticlesByTitle(keyword);

        return ResponseEntity.ok().body(articles);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<String> update(@RequestBody String info, @PathVariable String articleId){

        JSONObject jsonObject = new JSONObject(info);

        String title = jsonObject.getString("title");
        String content = jsonObject.getString("content");
        String tag = jsonObject.getString("tag");
        String category = jsonObject.getString("category");

        Boolean message = articleService.update(articleId, title, content, tag, category);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{userId}/{articleId}")
    public ResponseEntity<String> delete(@PathVariable(value="userId") String userId, @PathVariable(value="articleId") String articleId){
        boolean message = articleService.delete(userId, articleId);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{userId}/{articleId}")
    public ResponseEntity<String> recover(@PathVariable(value="userId") String userId, @PathVariable(value="articleId") String articleId){
        boolean message = articleService.recover(userId, articleId);

        if(message){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
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
}