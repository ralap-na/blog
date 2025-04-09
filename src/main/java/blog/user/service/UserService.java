package blog.user.service;

import blog.article.Repository;
import blog.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        repository.saveUser(user);

        return true;
    }
}
