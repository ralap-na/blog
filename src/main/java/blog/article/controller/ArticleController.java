package blog.article.controller;

import blog.article.service.ArticleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

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
}
