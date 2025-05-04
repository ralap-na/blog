package blog.chat;

import java.time.Instant;

public class Conversation {
    private String id;
    private String userId;
    private String content;
    private Instant date;

    public Conversation(String id, String userId, String content, Instant date) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
