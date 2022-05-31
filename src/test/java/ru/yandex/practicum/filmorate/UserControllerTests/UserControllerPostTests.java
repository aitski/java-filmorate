package ru.yandex.practicum.filmorate.UserControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerPostTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createUserTest() {

        User user = new User
                ("user@mail.ru", "user", "User", "1985-05-11");
        HttpEntity<User> request = new HttpEntity<>(user);
        //clearing map from previous tests
        restTemplate.delete("http://localhost:" + port + "/users");
        User userFromServer = restTemplate.postForObject
                ("http://localhost:" + port + "/users", request, User.class);
        user.setId(userFromServer.getId());
        assertNotNull(userFromServer);
        assertEquals(user, userFromServer);

    }

    @Test
    public void incorrectUserEmailTest() {

        User user = new User
                ("usermailru", "user", "User", "1985-05-11");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity
                ("http://localhost:" + port + "/users",request,User.class);
        assertEquals("400 BAD_REQUEST",responseEntity.getStatusCode().toString());
    }

    @Test
    public void incorrectLoginTest() {

        User user = new User
                ("user@mail.ru", "user login", "User", "1985-05-11");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity
                ("http://localhost:" + port + "/users",request,User.class);
        assertEquals("400 BAD_REQUEST",responseEntity.getStatusCode().toString());
    }

    @Test
    public void emptyNameTest() {

        User user = new User
                ("user@mail.ru", "userlogin", "", "1985-05-11");
        HttpEntity<User> request = new HttpEntity<>(user);
        User userFromServer = restTemplate.postForObject
                ("http://localhost:" + port + "/users", request, User.class);
        assertNotNull(userFromServer);
        assertEquals("userlogin", userFromServer.getName());
    }

    @Test
    public void birthdayInFutureTest() {

        User user = new User
                ("user@mail.ru", "user login", "User", "1985-05-11");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity
                ("http://localhost:" + port + "/users",request,User.class);
        assertEquals("400 BAD_REQUEST",responseEntity.getStatusCode().toString());
    }
    }