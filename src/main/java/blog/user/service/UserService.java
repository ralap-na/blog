package blog.user.service;

import blog.article.Bookmark;
import blog.article.Repository;
import blog.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private Repository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createUser(String username, String password) {
        if (repository.findUserByUsername(username).isPresent()) {
            return false;
        }

        String hashPassword = passwordEncoder.encode(password);
        User user = new User(UUID.randomUUID().toString(), username, hashPassword);

        user.addBookmark(new Bookmark(UUID.randomUUID().toString(), "default"));
        repository.saveUser(user);

        return true;
    }

    public boolean loginUser(String username, String password) {
        if (repository.findUserByUsername(username).isEmpty()) {
            return false;
        }

        User user = repository.findUserByUsername(username).get();

        return passwordEncoder.matches(password, user.getPassword());
    }

    public String getUserIdByUserName(String username) {
        return repository.findUserByUsername(username).get().getUserId();
    }

    public boolean updateUser(String userId, String username, String password) {
        User user = repository.findUserById(userId);

        String hashPassword = passwordEncoder.encode(password);
        user.update(username, hashPassword);

        repository.saveUser(user);

        return true;
    }

    public User getUser(String userId) {
        return repository.findUserById(userId);
    }

    public Map<String, String> getUsernames(List<String> userIds) {
        return repository.findUsernamesByUserId(userIds);
    }
}
