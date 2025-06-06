package blog.user;

import blog.article.Article;
import blog.feedback.Comment;
import blog.article.Bookmark;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private String password;

    private List<Article> articleList;
    private List<Article> deletedArticleList;
    private final List<Comment> commentList;
    private List<Bookmark> bookmarkList;

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        commentList = new ArrayList<>();
    }

    public void clear(){
        if (articleList != null) {
            articleList.clear();
        }
        if (deletedArticleList != null) {
            deletedArticleList.clear();
        }
    }

    public List<Article> getArticleList() {
        if (articleList == null) {
            articleList = new ArrayList<>();
        }
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public List<Article> getDeletedArticleList() {
        if (deletedArticleList == null) {
            deletedArticleList = new ArrayList<>();
        }
        return deletedArticleList;
    }

    public void setDeletedArticleList(List<Article> deletedArticleList) {
        this.deletedArticleList = deletedArticleList;
    }

    public String addArticle(Article article){
        if (article == null) return null;
        getArticleList().add(article);
        return article.getArticleId();
    }

    public Article updateArticle(String articleId, String title, String content, String tag, String category){
        Article article = findArticleById(articleId);
        if(article != null){
            article.update(title, content, tag, category);
            for (int i = 0; i < articleList.size(); i++) {
                if (articleList.get(i).getArticleId() == articleId) {
                    articleList.set(i, article);
                    break; // 找到後就退出迴圈
                }
            }
        }
        else{
            return null;
        }
        return article;
    }

    public Article findArticleById(String articleId){
        if (articleId == null || articleList == null) return null;

        for(Article article: articleList){
            if(article.getArticleId().equals(articleId)){
                return article;
            }
        }

        return null;
    }

    public Article findDeletedArticleById(String articleId){
        if (articleId == null || deletedArticleList == null) return null;

        for(Article article: deletedArticleList){
            if(article.getArticleId().equals(articleId)){
                return article;
            }
        }

        return null;
    }

    public boolean deleteArticle(String articleId) {
        if (articleId == null || articleList == null) return false;

        for (int i = 0; i < articleList.size(); i++) {
            if (articleList.get(i).getArticleId().equals(articleId)) {
                Article article = articleList.get(i);
                article.delete();
                articleList.remove(i);
                getDeletedArticleList().add(article);
                return true; // 刪除成功後立即返回
            }
        }
        return false; // 沒有找到符合的文章
    }

    public boolean recoverArticle(String articleId) {
        if (articleId == null || deletedArticleList == null) return false;

        for (int i = 0; i < deletedArticleList.size(); i++) {
            if (deletedArticleList.get(i).getArticleId().equals(articleId)) {
                Article article = deletedArticleList.get(i);
                article.recover();
                deletedArticleList.remove(i);
                getArticleList().add(article);
                return true; // 刪除成功後立即返回
            }
        }
        return false; // 沒有找到符合的文章
    }
  
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void update(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public Comment getCommentById(String commentId) {
        return commentList.stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElse(null);
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void deleteComment(String commentId) {
        commentList.removeIf(comment -> comment.getId().equals(commentId));
    }
  
    public List<Bookmark> getBookmarkList() {
        if (bookmarkList == null) {
            bookmarkList = new ArrayList<>();
        }
        return bookmarkList;
    }
    public Bookmark findBookmarkById(String bookmarkId){
        for (Bookmark bookmark : bookmarkList) {
            if (bookmark.getBookmarkId().equals(bookmarkId)) {
                return bookmark;
            }
        }
        return null;
    }
    public String addBookmark(Bookmark bookmark){
        if (bookmark == null)
            return null;
        getBookmarkList().add(bookmark);
        return bookmark.getBookmarkId();
    }
    public boolean deleteBookmark(Bookmark bookmark){
        if (bookmark == null || bookmarkList == null) return false;

        bookmarkList.remove(bookmark);
        return true;
    }
}
