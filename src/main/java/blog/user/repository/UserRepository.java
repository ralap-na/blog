package blog.user.repository;

import blog.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, String> {
     Optional<User> findByUsername(String username);
}