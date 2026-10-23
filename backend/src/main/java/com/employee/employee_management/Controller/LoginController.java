
package com.employee.employee_management.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.employee.employee_management.Constant.AuthenticationConstant;
import com.employee.employee_management.Persitance.entity.User;
import com.employee.employee_management.Persitance.repository.UserRepository;

@Component
@RestController
@RequestMapping(AuthenticationConstant.AUTHENTICATION)
public class LoginController {

    private static final Logger logger =
            LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
}

    @RequestMapping(
            value = AuthenticationConstant.AUTHENTICATION_LOGINS,
            method = RequestMethod.POST
    )
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, Object>> login(
            HttpServletRequest request,
            @RequestBody Map<String, String> auth) {

        Map<String, Object> result = new HashMap<>();

        try {
            String username = auth.get("username");
            String password = auth.get("password");

            if (username == null || username.trim().isEmpty()) {
                result.put("success", false);
                result.put("code",
                        AuthenticationConstant.INVALID_USER_NOT_FOUND_ERROR_CODE);
                result.put("message",
                        AuthenticationConstant.INVALID_USER_NOT_FOUND_ERROR_DESC);

                return ResponseEntity.badRequest().body(result);
            }

            if (password == null || password.isEmpty()) {
                result.put("success", false);
                result.put("code",
                        AuthenticationConstant.INVALID_PASSWORD_USER_EMPTY_ERROR_CODE);
                result.put("message",
                        AuthenticationConstant.INVALID_PASSWORD_USER_EMPTY_ERROR_DESC);

                return ResponseEntity.badRequest().body(result);
            }

            Optional<User> userOptional =
                    userRepository.findByUsername(username);

            if (userOptional.isEmpty()) {
                userOptional = userRepository.findByEmail(username);
            }

            if (userOptional.isEmpty()) {
                result.put("success", false);
                result.put("code",
                        AuthenticationConstant.INVALID_USER_NOT_FOUND_ERROR_CODE);
                result.put("message",
                        AuthenticationConstant.INVALID_USER_NOT_FOUND_ERROR_DESC);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(result);
            }

            User user = userOptional.get();

            // Sementara: gunakan perbandingan langsung.
            // Ganti dengan BCrypt PasswordEncoder sebelum produksi.
            if (user.getPassword() == null
                    || !user.getPassword().equals(password)) {

                result.put("success", false);
                result.put("code",
                        AuthenticationConstant.INVALID_USER_NOT_FOUND_ERROR_CODE);
                result.put("message",
                        AuthenticationConstant.INVALID_USER_NOT_FOUND_ERROR_DESC);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(result);
            }

            if (user.getStatus() == null
                    || !AuthenticationConstant.STATUS_ACTIVE
                            .equalsIgnoreCase(user.getStatus())) {

                result.put("success", false);
                result.put("code",
                        AuthenticationConstant.INVALID_USER_STATUS_ERROR_CODE);
                result.put("message",
                        AuthenticationConstant.INVALID_USER_STATUS_ERROR_DESC);

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(result);
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setMaxInactiveInterval(1800);

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("status", user.getStatus());

            result.put("success", true);
            result.put("code",
                    AuthenticationConstant.SUCCESS_LOGIN_CODE);
            result.put("message",
                    AuthenticationConstant.SUCCESS_LOGIN_MESSAGE);
            result.put("user", userData);

            logger.info("Login successful for user ID: {}", user.getId());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error while processing login.", e);

            result.put("success", false);
            result.put("code", "500");
            result.put("message", "An internal server error occurred.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }
}
