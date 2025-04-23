package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    @Test
    void getInstance() {
        SessionManager sessionManager1 = SessionManager.getInstance();
        SessionManager sessionManager2 = SessionManager.getInstance();
        assertSame(sessionManager1, sessionManager2, "SessionManager should be a singleton");
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void getUser() {
    }

    @Test
    void isLoggedIn() {
    }

    @Test
    void setToken() {
    }

    @Test
    void getToken() {
    }

    @Test
    void getUserName() {
    }
}