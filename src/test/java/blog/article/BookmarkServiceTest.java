package blog.article;

import blog.article.service.BookmarkService;
import blog.user.User;
import blog.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private UserService userService;
    @Autowired
    private Repository repository;
    private String testerId;

    @BeforeEach
    void setUp() {
        userService.createUser("Tester", "Tester");
        testerId = repository.findUserByUsername("Tester").get().getUserId();
    }

    @AfterEach
    public void tearDown() {
        // 清空 Repository 裡的資料
        repository.clear();
        repository.deleteUser(testerId);
    }
    @Test
    public void addArticle(){
        Bookmark bookmark = new Bookmark("b1", "Bookmark-1");
        User user = repository.findUserById(testerId);
        user.addBookmark(bookmark);

        Article article = new Article();
        article.setArticleId("a1");
        article.setUserId("u1");
        repository.saveArticle(article);
        boolean message = bookmarkService.addArticle(bookmark.getBookmarkId(), article.getArticleId());

        assertTrue(message);
    }

    @Test
    public void deleteArticle(){
        Article article = new Article();
        article.setArticleId("a1");
        article.setUserId("u1");
        Bookmark bookmark = new Bookmark("b1", "Bookmark-1");
        User user = repository.findUserById(testerId);
        user.addBookmark(bookmark);

        repository.saveArticle(article);
        bookmarkService.addArticle("b1", article.getArticleId());
        boolean message = bookmarkService.deleteArticle("b1", article.getArticleId());

        assertTrue(message);
    }

    @Test
    public void getArticleIds(){
        Article article1 = new Article();
        article1.setArticleId("a1");
        article1.setUserId("u1");

        Article article2 = new Article();
        article2.setArticleId("a2");
        article2.setUserId("u1");

        Bookmark bookmark = new Bookmark("b1", "Bookmark-1");
        User user = repository.findUserById(testerId);
        user.addBookmark(bookmark);

        repository.saveArticle(article1);
        repository.saveArticle(article2);
        bookmarkService.addArticle("b1", article1.getArticleId());
        bookmarkService.addArticle("b1", article2.getArticleId());

        List<String> articleIds = bookmarkService.getArticleIds("b1");

        assertTrue(articleIds.contains("a1"));
        assertTrue(articleIds.contains("a2"));
        assertEquals(2, articleIds.size());
    }

    @Test
    public void getBookmarkIds(){
        String bookmarkId_1 = "b1";
        String bookmarkName_1 = "Bookmark-1";
        String bookmarkId_2 = "b2";
        String bookmarkName_2 = "Bookmark-2";
        Bookmark bookmark_1 = new Bookmark(bookmarkId_1, bookmarkName_1);
        Bookmark bookmark_2 = new Bookmark(bookmarkId_2, bookmarkName_2);
        User user = repository.findUserById(testerId);
        user.addBookmark(bookmark_1);
        user.addBookmark(bookmark_2);

        List<Bookmark> bookmarkList = bookmarkService.getBookmarks(testerId);

        assertTrue(bookmarkList.contains(bookmark_1));
        assertTrue(bookmarkList.contains(bookmark_2));
        assertEquals(3, bookmarkList.size());
    }

    @Test
    public void addBookmark(){
        String bookmarkId = "b1";
        String bookmarkName = "Bookmark";
        boolean message = bookmarkService.addBookmark(bookmarkId, bookmarkName, testerId);
        List<Bookmark> bookmarkList = bookmarkService.getBookmarks(testerId);

        assertTrue(message);
        assertEquals(2, bookmarkList.size());
    }

    @Test
    public void deleteBookmark(){
        String bookmarkId_1 = "b1";
        String bookmarkName_1 = "Bookmark-1";
        String bookmarkId_2 = "b2";
        String bookmarkName_2 = "Bookmark-2";
        Bookmark bookmark_1 = new Bookmark(bookmarkId_1, bookmarkName_1);
        Bookmark bookmark_2 = new Bookmark(bookmarkId_2, bookmarkName_2);
        User user = repository.findUserById(testerId);
        user.addBookmark(bookmark_1);
        user.addBookmark(bookmark_2);
        List<Bookmark> bookmarkList = bookmarkService.getBookmarks(testerId);

        assertEquals(3, bookmarkList.size());
        bookmarkService.deleteBookmark(bookmarkId_2, testerId);
        bookmarkList = bookmarkService.getBookmarks(testerId);
        assertEquals(2, bookmarkList.size());
    }
}
