package blog.article.service;

import blog.article.Bookmark;
import blog.article.Repository;
import blog.user.User;
import blog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {
    @Autowired
    private Repository repository;
    @Autowired
    private UserService userService;

    public boolean addArticle(String bookmarkId, String articleId) {
        Bookmark bookmark = repository.findBookmarkById(bookmarkId);

        if (bookmark == null) {
            return false;
        }

        bookmark.addArticle(articleId);
        return true;
    }

    public boolean deleteArticle(String bookmarkId, String articleId) {
        Bookmark bookmark = repository.findBookmarkById(bookmarkId);

        if (bookmark == null) {
            return false;
        }

        bookmark.deleteArticle(articleId);
        return true;
    }

    public List<String> getArticleIds(String bookmarkId) {
        Bookmark bookmark = repository.findBookmarkById(bookmarkId);

        if (bookmark == null) {
            return null;
        }

        return bookmark.getArticleIds();
    }

    public List<Bookmark> getBookmarks(String userId) {
        User user = userService.getUser(userId);

        return user.getBookmarkList();
    }

    public boolean addBookmark(String bookmarkId, String bookmarkName, String userId) {
        Bookmark bookmark = new Bookmark(bookmarkId, bookmarkName);
        User user = userService.getUser(userId);
        if (user == null) {
            return false;
        }
        user.addBookmark(bookmark);
        repository.saveUser(user);
        return true;
    }

    public boolean deleteBookmark(String bookmarkId, String userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return false;
        }
        Bookmark bookmark = repository.findBookmarkById(bookmarkId);
        user.deleteBookmark(bookmark);
        return true;
    }
}