package blog.user.controller;

import blog.user.User;
import blog.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody String userInfo) {
        JSONObject jsonObject = new JSONObject(userInfo);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        boolean result = userService.createUser(username, password);
        if (result) {
            return ResponseEntity.ok("User created successfully.");
        } else {
            return ResponseEntity.internalServerError().body("Failed to create user.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody String userInfo, HttpSession session) {
        JSONObject jsonObject = new JSONObject(userInfo);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        boolean result = userService.loginUser(username, password);
        if (result) {
            String userId = userService.getUserIdByUserName(username);

            session.setAttribute("userId", userId);

            return ResponseEntity.ok("User login successfully.");
        } else {
            return ResponseEntity.internalServerError().body("Failed to login user.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody String userInfo, HttpSession session) {
        String sessionUserId = (String) session.getAttribute("userId");

        if (sessionUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("not login");
        }

        JSONObject jsonObject = new JSONObject(userInfo);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        boolean result = userService.updateUser(sessionUserId, username, password);
        if (result) {
            return ResponseEntity.ok("User update successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update user.");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in.");
        }

        User user = userService.getUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in.");
        }

        User user = userService.getUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
