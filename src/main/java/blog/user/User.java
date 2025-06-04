package blog.user;

import blog.article.Article;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private String password;

    private List<Article> articleList;

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public void clear(){
        articleList.clear();
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
}
