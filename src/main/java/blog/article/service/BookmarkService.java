package blog.article.service;

import blog.article.Bookmark;
import blog.article.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {
    @Autowired
    private Repository repository;

    public boolean addArticle(String userId, String articleId) {
        Bookmark bookmark = repository.findBookmarkByUserId(userId);

        if (bookmark == null) {
            bookmark = new Bookmark(userId); // first time add article
            repository.saveBookmark(bookmark);
        }

        bookmark.addArticle(articleId);
        return true;
    }

    public boolean deleteArticle(String userId, String articleId) {
        Bookmark bookmark = repository.findBookmarkByUserId(userId);

        if (bookmark == null) {
            return false;
        }

        bookmark.deleteArticle(articleId);
        return true;
    }
}
