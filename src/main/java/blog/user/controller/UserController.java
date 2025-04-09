package blog.user.controller;

import blog.user.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
