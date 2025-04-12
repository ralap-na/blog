package blog.article;

import net.minidev.json.JSONObject;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;

import java.time.Instant;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Repository repository; // 注入 Repository 以準備測試數據

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        // 設定基礎 URL，RANDOM_PORT 會自動分配測試端口
        baseUrl = "/article";

        repository.clear();
        repository.saveArticle(new Article("1", "1", "Original Title", "Original Content", "Original Tag", "Original Category", Instant.now(), false));
        repository.saveArticle(new Article("1", "2", "Expected Title", "Expected Content", "Expected Tag", "Expected Category", Instant.now(), false));
    }

    @Test
    public void createArticleSuccess() {
        // 準備測試文章
        JSONObject requestBody = new JSONObject();
        requestBody.put("userId", "1");
        requestBody.put("articleId", "3");
        requestBody.put("title", "Created Title");
        requestBody.put("content", "Created Content");
        requestBody.put("tag", "Created tag");
        requestBody.put("category", "Created category");
        requestBody.put("date", "2025-04-06 02:39");

        // 設定Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        // 發送 POST request
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/",
                HttpMethod.POST,
                request,
                String.class
        );

        // 檢查reponse status code 是否是 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("3", response.getBody());

        // 驗證 Repository 中的資料是否更新
        Article updatedArticle = repository.findArticleById("3");
        assertEquals("1", updatedArticle.getUserId());
        assertEquals("Created Title", updatedArticle.getTitle());
        assertEquals("Created Content", updatedArticle.getContent());
        assertEquals("Created tag", updatedArticle.getTag());
        assertEquals("Created category", updatedArticle.getCategory());
    }

    @Test
    public void getArticleSuccess() throws JSONException {
        String articleId = "1";

        // 設定Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        // 發送 GET request
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + articleId,
                HttpMethod.GET,
                request,
                String.class
        );

        // 檢查reponse status code 是否是 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 驗證 reponse 中的資料是否正確
        org.json.JSONObject jsonObject = new org.json.JSONObject(response.getBody());

        assertEquals("1", jsonObject.getString("articleId"));
        assertEquals("1", jsonObject.getString("userId"));
        assertEquals("Original Title", jsonObject.getString("title"));
        assertEquals("Original Content", jsonObject.getString("content"));
        assertEquals("Original Tag", jsonObject.getString("tag"));
        assertEquals("Original Category", jsonObject.getString("category"));
    }

    @Test
    public void getArticlesByUserId(){
        String userId = "1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<Collection<Article>> response = restTemplate.exchange(
                baseUrl + "/user/" + userId,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Collection<Article>>() {}
        );

        Collection<Article> articles = response.getBody();

        // 驗證狀態碼
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 驗證 body 不為 null
        assertNotNull(articles);

        // 驗證回傳文章數量大於 0
        assertFalse(articles.isEmpty());

        // 驗證文章 userId 為 "1"
        for (Article article : articles) {
            assertEquals(userId, article.getUserId(), "Each article should belong to userId = 1");
        }

    }

    @Test
    public void getArticlesByTitle(){
        String keyword = "Expected";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<Collection<Article>> response = restTemplate.exchange(
                baseUrl + "/title/" + keyword,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Collection<Article>>() {}
        );

        Collection<Article> articles = response.getBody();

        // 驗證狀態碼
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 驗證 body 不為 null
        assertNotNull(articles);

        // 驗證回傳文章數量大於 0
        assertFalse(articles.isEmpty());

        // 驗證文章 keyword 為 "Expected"
        for (Article article : articles) {
            assertTrue(article.getTitle().contains("Expected"), "Each article's title should contain \"Expected\"");
        }

    }

    @Test
    void updateArticleSuccess() {
        // 準備測試文章
        String articleId = "1";
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Updated Title");
        requestBody.put("content", "Updated Content");
        requestBody.put("tag", "Updated Tag");
        requestBody.put("category", "Updated Category");

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

        // 檢查reponse status code 是否是 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 驗證 Repository 中的資料是否更新
        Article updatedArticle = repository.findArticleById(articleId);
        assertEquals("Updated Title", updatedArticle.getTitle());
        assertEquals("Updated Content", updatedArticle.getContent());
        assertEquals("Updated Tag", updatedArticle.getTag());
        assertEquals("Updated Category", updatedArticle.getCategory());
    }

    @Test
    void updateArticleFailure() {
        // 準備測試文章
        String articleId = "999";
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Invalid Title");
        requestBody.put("content", "Invalid Content");
        requestBody.put("tag", "invalid");
        requestBody.put("category", "invalid");

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

        // 檢查reponse status code 是否是 500 Internal Server Error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteArticle(){
        String userId = "1";
        String articleId = "1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + userId + "/" + articleId,
                HttpMethod.DELETE,
                request,
                String.class
        );

        // 檢查reponse status code 是否是 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void recoverArticle(){
        String userId = "1";
        String articleId = "1";

        repository.delete(articleId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + userId + "/" + articleId,
                HttpMethod.PUT,
                request,
                String.class
        );

        // 檢查reponse status code 是否是 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addToBookmark() {
        String userId = "u1";
        String articleId = "a1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/bookmark/" + userId + "/" + articleId,
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteFromBookmark() {
        String userId = "u1";
        String articleId = "a1";
        Article article = new Article();
        article.setArticleId(articleId);
        article.setUserId(userId);
        repository.saveArticle(article);

        Bookmark bookmark = new Bookmark("u1");
        repository.saveBookmark(bookmark);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/bookmark/" + userId + "/" + articleId,
                HttpMethod.DELETE,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getArticleIdsFromBookmarkByUserId() {
        Article article1 = new Article();
        article1.setArticleId("a1");
        article1.setUserId("u1");
        repository.saveArticle(article1);

        Article article2 = new Article();
        article2.setArticleId("a2");
        article2.setUserId("u1");
        repository.saveArticle(article2);

        String userId = "u1";
        Bookmark bookmark = new Bookmark(userId);
        repository.saveBookmark(bookmark);

        bookmark.addArticle(article1.getArticleId());
        bookmark.addArticle(article2.getArticleId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/bookmark/" + userId,
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
