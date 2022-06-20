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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerPutTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void updateUserTest() {

        restTemplate.delete("http://localhost:" + port + "/users");
        User user = new User
                ("user@mail.ru", "user", "User", "1985-05-11");
        HttpEntity<User> request = new HttpEntity<>(user);
        //clearing map from previous tests

        restTemplate.postForLocation("http://localhost:" + port + "/users",request);

        user.setEmail("newEmail@mail.ru");
        user.setId(1);

        HttpEntity<User> request2 = new HttpEntity<>(user);
        restTemplate.put("http://localhost:" + port + "/users", request2);
        String mapFromServer = restTemplate.getForObject
                ("http://localhost:" + port + "/users",String.class);

        assertEquals("[{\"id\":1,\"friends\":{},\"email\":\"newEmail@mail.ru\",\"login\":\"user\",\"name\":\"User\",\"birthday\":\"1985-05-11\"}]",
                mapFromServer);

    }
    }