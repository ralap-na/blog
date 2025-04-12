package blog.user;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void updateUser() {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "username", "password");

        user.update("new username", "new password");

        assertEquals("new username", user.getUsername());
        assertEquals("new password", user.getPassword());

    }
}
