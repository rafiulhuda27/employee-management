package com.employee.employee_management.Controller;

import java.lang.classfile.ClassFile.Option;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.employee_management.Constant.AuthenticationConstant;
import com.employee.employee_management.Persitance.entity.User;
import com.employee.employee_management.Persitance.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Component 
@RestController 
@RequestMapping(AuthenticationConstant.AUTHENTICATION)
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(AuthenticationConstant.AUTHENTICATION_LOGINS)
    @CrossOrigin (origins = "*")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse response, @RequestBody  Map<String, String> auth) {
        Map<String, Object> result = new HashMap<>();

        try{
            String username = auth.get("username");
            String password = auth.get("password");

            if (username == null || username.trim().isEmpty()) {
                result.put("success", false);
                result.put("code", AuthenticationConstant.INVALID_PASSWORD_USER_EMPTY_ERROR_CODE);
                result.put("message", AuthenticationConstant.INVALID_PASSWORD_USER_EMPTY_ERROR_DESC);

                return ResponseEntity.badRequest().body(result);
            }

            if (password == null || password.isEmpty()) {
                result.put("success", false);
                result.put("code", AuthenticationConstant.INVALID_PASSWORD_USER_EMPTY_ERROR_CODE);
                result.put("message", AuthenticationConstant.INVALID_PASSWORD_USER_EMPTY_ERROR_DESC);
                return ResponseEntity.badRequest().body(result);
            }
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                userOptional = userRepository.findByEmail(password);
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

            /*
             * Temporary password verification.
             * Replace this with BCrypt PasswordEncoder before production.
             */
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

            if (!AuthenticationConstant.STATUS_ACTIVE
                    .equalsIgnoreCase(user.getStatus())) {

                result.put("success", false);
                result.put("code",
                        AuthenticationConstant.STATUS_ACTIVE);
                result.put("message",
                        AuthenticationConstant.STATUS_INACTIVE);

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

    @RequestMapping(AuthenticationConstant.AUTHENTICATION_LOGOUT)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, Object>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();

        try {
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("userId") == null) {
                result.put("success", false);
                result.put("code", "401");
                result.put("message", "User is not logged in.");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(result);
            }

            session.invalidate();

            result.put("success", true);
            result.put("code",
                    AuthenticationConstant.SUCCESS_LOGOUT_CODE);
            result.put("message",
                    AuthenticationConstant.SUCCESS_LOGOUT_MESSAGE);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error while processing logout.", e);

            result.put("success", false);
            result.put("code", "500");
            result.put("message", "An internal server error occurred.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

    @RequestMapping(AuthenticationConstant.AUTHENTICATION_USER)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, Object>> authenticatedUser(
            HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            result.put("success", false);
            result.put("code", "401");
            result.put("message", "User is not authenticated.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(result);
        }

        result.put("success", true);
        result.put("userId", session.getAttribute("userId"));
        result.put("username", session.getAttribute("username"));

        return ResponseEntity.ok(result);
    }
    
}
