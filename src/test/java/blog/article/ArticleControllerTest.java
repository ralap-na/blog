package blog.article;

import blog.user.User;
import net.minidev.json.JSONObject;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

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
        requestBody.put("date", "2025-04-12T15:30:24.517Z");

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

        // 驗證 Repository 中的資料是否更新
        Article updatedArticle = repository.findArticleById(response.getBody());
        assertEquals("1", updatedArticle.getUserId());
        assertEquals("Created Title", updatedArticle.getTitle());
        assertEquals("Created Content", updatedArticle.getContent());
        assertEquals("Created tag", updatedArticle.getTag());
        assertEquals("Created category", updatedArticle.getCategory());
    }

    @Test
    public void createArticleFail() {
        // 準備測試文章
        JSONObject requestBody = new JSONObject();
        requestBody.put("userId", "1");
        requestBody.put("articleId", "3");
        requestBody.put("title", "");
        requestBody.put("content", "Created Content");
        requestBody.put("tag", "Created tag");
        requestBody.put("category", "Created category");
        requestBody.put("date", "2025-04-12T15:30:24.517Z");

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

        // 檢查reponse status code 是否是 500 INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Title, Category, and Content cannot Empty.", response.getBody());
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
    public void getAllArticles(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<Collection<Article>> response = restTemplate.exchange(
                baseUrl + "/all",
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

        assertEquals(2, articles.size());

    }

    @Test
    public void getArticlesByConditions(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/condition")
                .queryParam("title", "Expected Title")
                .queryParam("category", "Expected Category")
                .queryParam("tag", "Expected Tag");

        ResponseEntity<Collection<Article>> response = restTemplate.exchange(
                builder.toUriString(),
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

        assertEquals(1, articles.size());

    }

    @Test
    public void getAllDeletedArticlesByUserId(){
        repository.delete("1");

        String userId = "1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<Collection<Article>> response = restTemplate.exchange(
                baseUrl + "/all/deleted/" + userId,
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

        assertEquals(1, articles.size());

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
    void updateArticleWithInValidIdFailure() {
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
    void updateArticleWithInValidDataFailure() {
        // 準備測試文章
        String articleId = "1";
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "");
        requestBody.put("content", "");
        requestBody.put("tag", "invalid");
        requestBody.put("category", "");

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

        // 檢查reponse status code 是否是 500 INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Title, Category, and Content cannot Empty.", response.getBody());
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
        String bookmarkId = "b1";
        String bookmarkName = "Bookmark-1";
        Bookmark bookmark = new Bookmark(bookmarkId, bookmarkName, userId);
        repository.saveBookmark(bookmark);
        String articleId = "a1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/bookmark/" + bookmarkId + "/" + articleId,
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

        String bookmarkId = "b1";
        String bookmarkName = "Bookmark-1";
        Bookmark bookmark = new Bookmark(bookmarkId, bookmarkName, userId);
        bookmark.addArticle(article.getArticleId());
        repository.saveBookmark(bookmark);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/bookmark/" + bookmarkId + "/" + articleId,
                HttpMethod.DELETE,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getArticleIdsFromBookmarkByBookmarkId() {
        Article article1 = new Article();
        article1.setArticleId("a1");
        article1.setUserId("u1");
        repository.saveArticle(article1);

        Article article2 = new Article();
        article2.setArticleId("a2");
        article2.setUserId("u1");
        repository.saveArticle(article2);

        String userId = "u1";
        String bookmarkId = "b1";
        String bookmarkName = "Bookmark-1";
        Bookmark bookmark = new Bookmark(bookmarkId, bookmarkName, userId);
        repository.saveBookmark(bookmark);

        bookmark.addArticle(article1.getArticleId());
        bookmark.addArticle(article2.getArticleId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/bookmark/" + bookmarkId,
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getBookmarksByUserId() {
        String userId = "u1";
        String bookmarkId_1 = "b1";
        String bookmarkName_1 = "Bookmark-1";
        String bookmarkId_2 = "b2";
        String bookmarkName_2 = "Bookmark-2";
        Bookmark bookmark_1 = new Bookmark(bookmarkId_1, bookmarkName_1, userId);
        Bookmark bookmark_2 = new Bookmark(bookmarkId_2, bookmarkName_2, userId);
        repository.saveBookmark(bookmark_1);
        repository.saveBookmark(bookmark_2);

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

    @Test
    public void createCategorySuccess_withAdminLogin() {
        // 先登入取得 cookie
        String cookie = loginAsAdminAndGetCookie();

        // 設定帶 Cookie 的 Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, cookie);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        System.out.println(baseUrl + "/category/create");
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/category/create?name=Health",
                HttpMethod.POST,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Success"));
    }

    @Test
    public void createCategoryWithEmptyName_shouldFail() {
        // 先登入取得 cookie
        String cookie = loginAsAdminAndGetCookie();

        // 設定帶 Cookie 的 Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, cookie);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        // 空白名稱
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/category/create?name= ",
                HttpMethod.POST,
                request,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Category name is required.", response.getBody());
    }

    @Test
    public void deleteCategorySuccess_withAdminLogin() {
        // 先新增一個 category
        Category category = new Category(UUID.randomUUID().toString(), "TempDelete");
        repository.saveCategory(category);

        // 先登入取得 cookie
        String cookie = loginAsAdminAndGetCookie();

        // 設定帶 Cookie 的 Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, cookie);

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/category/delete/" + category.getId(),
                HttpMethod.DELETE,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success: Category deleted.", response.getBody());
    }

    @Test
    public void deleteCategoryNotFound_shouldFail() {
        // 先模擬 Admin 登入
        String cookie = loginAsAdminAndGetCookie();

        String fakeId = "non-existent-id";

        // 設定帶 Cookie 的 Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, cookie);

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/category/delete/" + fakeId,
                HttpMethod.DELETE,
                request,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Category not found.", response.getBody());
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String loginAsAdminAndGetCookie() {
        // 先在 Repository 放一個 Admin user

        String password = passwordEncoder.encode("admin-password");
        User adminUser = new User("admin-id", "Admin", password);
        repository.saveUser(adminUser);

        // 用 JSON 格式登入
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "Admin");
        requestBody.put("password", "admin-password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/users/login",
                HttpMethod.POST,
                request,
                String.class
        );

        // 檢查登入是否成功
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 把 Set-Cookie 抓出來
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie); // 順便驗證真的有拿到 Cookie

        return cookie;
    }

    @Test
    public void getAllCategories_shouldReturnList() {
        repository.saveCategory(new Category(UUID.randomUUID().toString(), "X"));
        repository.saveCategory(new Category(UUID.randomUUID().toString(), "Y"));

        ResponseEntity<Category[]> response = restTemplate.getForEntity(
                baseUrl + "/category",
                Category[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2); // 根據初始化或額外新增的
    }
}
