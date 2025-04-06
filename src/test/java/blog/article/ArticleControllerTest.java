package blog.article;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Repository repository; // 注入 Repository 以準備測試數據

    private String baseUrl;

    @BeforeEach
    void setUp() {
        // 設定基礎 URL，RANDOM_PORT 會自動分配測試端口
        baseUrl = "/article";

        repository.saveArticle(new Article("1", "1", "Original Title", "Original Content", "oldTag", "oldCategory", Instant.now(), false));
    }

    @Test
    void testUpdateArticleSuccess() {
        // 準備測試數據
        String articleId = "1";
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Updated Title");
        requestBody.put("content", "Updated Content");
        requestBody.put("tag", "tech");
        requestBody.put("category", "blog");

        // 設定Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        // 發送 PUT request
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + articleId,
                HttpMethod.PUT,
                request,
                String.class
        );

        // 檢查響應狀態碼是否是 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 驗證 Repository 中的資料是否更新
        Article updatedArticle = repository.findArticleById(articleId);
        assertEquals("Updated Title", updatedArticle.getTitle());
        assertEquals("Updated Content", updatedArticle.getContent());
        assertEquals("tech", updatedArticle.getTag());
        assertEquals("blog", updatedArticle.getCategory());
    }

    @Test
    void testUpdateArticleFailure() {
        String articleId = "999";
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Invalid Title");
        requestBody.put("content", "Invalid Content");
        requestBody.put("tag", "invalid");
        requestBody.put("category", "invalid");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + articleId,
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
