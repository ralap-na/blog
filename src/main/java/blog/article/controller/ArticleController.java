package blog.article.controller;

import blog.article.Article;
import blog.article.service.ArticleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    private Instant converStringToInstant(String date){
        String pattern = "yyyy-MM-dd HH:mm";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(date, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody String info){
        JSONObject jsonObject = new JSONObject(info);
        String articleId = jsonObject.getString("articleId");
        String userId = jsonObject.getString("userId");
        String title = jsonObject.getString("title");
        String content = jsonObject.getString("content");
        String tag = jsonObject.getString("tag");
        String category = jsonObject.getString("category");
        Instant date = converStringToInstant(jsonObject.getString("date"));

        articleId = articleService.create(userId, articleId, title, content, tag, category, date);

        if(articleId != null){
            return ResponseEntity.ok().body(articleId);
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
}
