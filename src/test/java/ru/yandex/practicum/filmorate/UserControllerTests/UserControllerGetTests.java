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
public class UserControllerGetTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUserTest() {

        User user1 = new User
                ("user1@mail.ru", "user1", "User1", "1985-05-11");
        User user2 = new User
                ("user2@mail.ru", "user2", "User2", "1985-05-11");

        HttpEntity<User> request1 = new HttpEntity<>(user1);
        HttpEntity<User> request2 = new HttpEntity<>(user2);
        //clearing map from previous tests
        restTemplate.delete("http://localhost:" + port + "/users");
        restTemplate.postForLocation("http://localhost:" + port + "/users", request1);
        restTemplate.postForLocation("http://localhost:" + port + "/users", request2);

        String mapFromServer = restTemplate.getForObject
                ("http://localhost:" + port + "/users", String.class);

        assertEquals("{\"0\":{\"id\":0,\"email\":\"user1@mail.ru\",\"login\":\"user1\",\"name\":\"User1\",\"birthday\":\"1985-05-11\"}," +
                        "\"1\":{\"id\":1,\"email\":\"user2@mail.ru\",\"login\":\"user2\",\"name\":\"User2\",\"birthday\":\"1985-05-11\"}}",
                mapFromServer);
    }
}