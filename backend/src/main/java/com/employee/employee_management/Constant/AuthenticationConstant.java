package com.employee.employee_management.Constant;

public final class AuthenticationConstant {

    private AuthenticationConstant () {
        
    }
    public static final String AUTHENTICATION = "/api/auth";
    public static final String AUTHENTICATION_LOGINS = "/api/login";
    public static final String AUTHENTICATION_LOGOUT = "/api/logout";
    public static final String AUTHENTICATION_USER= "/api/user";
    public static final String AUTHENTICATION_TOKEN = "/api/user";


    public static final String JWT_CONFIG_PARSER_HEADER = "security.jwt.header";
    public static final String CONFIG_JWT_ENABLE = "security.jwt.enable";
    public static final String CONFIG_SESSION_ENABLE = "security.session.enabled";

    public static final String JWT_USERID_CLAIM = "userid";
    public static final String JWT_USERNAME_CLAIM = "username";

    public static final String ID_TOKEN_SESSION = "token_session";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_PENDING = "pending";

    public static final String STATUS_INVALID_USER_EMPTY_ERROR_CODE = "400";
    public static final String STATUS_INVALID_USER_EMPTY_ERROR_DESC = "Username or email is required";

    public static final String INVALID_PASSWORD_USER_EMPTY_ERROR_CODE = "400";
    public static final String INVALID_PASSWORD_USER_EMPTY_ERROR_DESC = "Password is required";

    public static final String INVALID_USER_NOT_FOUND_ERROR_CODE = "401";
    public static final String INVALID_USER_NOT_FOUND_ERROR_DESC = "Account is not active";

    public static final String SUCCESS_LOGIN_CODE = "200";
    public static final String SUCCESS_LOGIN_MESSAGE = "Login successful.";

    public static final String SUCCESS_LOGOUT_CODE = "200";
    public static final String SUCCESS_LOGOUT_MESSAGE = "Logout successful.";

}
