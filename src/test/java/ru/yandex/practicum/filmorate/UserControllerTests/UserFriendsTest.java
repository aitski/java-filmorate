package ru.yandex.practicum.filmorate.UserControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserFriendsTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void friendsTest() {

        User user0 = new User
                ("user0@mail.ru", "user0", "User0", "1985-05-11");
        User user1 = new User
                ("user1@mail.ru", "user1", "User1", "1985-05-11");
        User user2 = new User
                ("user2@mail.ru", "user2", "User2", "1985-05-11");
        User user3 = new User
                ("user3@mail.ru", "user3", "User3", "1985-05-11");

        HttpEntity<User> request3 = new HttpEntity<>(user0);
        HttpEntity<User> request4 = new HttpEntity<>(user1);
        HttpEntity<User> request5 = new HttpEntity<>(user2);
        HttpEntity<User> request6 = new HttpEntity<>(user3);

        restTemplate.delete("http://localhost:" + port + "/films");
        restTemplate.delete("http://localhost:" + port + "/users");

        restTemplate.postForLocation("http://localhost:" + port + "/users", request3);
        restTemplate.postForLocation("http://localhost:" + port + "/users", request4);
        restTemplate.postForLocation("http://localhost:" + port + "/users", request5);
        restTemplate.postForLocation("http://localhost:" + port + "/users", request6);

        //adding friends
        restTemplate.put("http://localhost:" + port + "/users/2/friends/1", null);
        restTemplate.put("http://localhost:" + port + "/users/3/friends/1", null);
        restTemplate.put("http://localhost:" + port + "/users/4/friends/1", null);
        restTemplate.put("http://localhost:" + port + "/users/4/friends/2", null);
        restTemplate.delete("http://localhost:" + port + "/users/3/friends/1");

        //check add,delete friend and list of friends
        String friendsList = restTemplate.getForObject(
                "http://localhost:" + port + "/users/1/friends", String.class);
        String friendsList2 = restTemplate.getForObject(
                "http://localhost:" + port + "/users/2/friends", String.class);
        //list of common friends
        String commonFriendsList = restTemplate.getForObject(
                "http://localhost:" + port + "/users/1/friends/common/2", String.class);

        assertEquals("[{\"id\":2,\"friends\":{\"4\":\"PENDING\"},\"email\":\"user1@mail.ru\",\"login\":\"user1\",\"name\":\"User1\",\"birthday\":\"1985-05-11\"},{\"id\":4,\"friends\":{},\"email\":\"user3@mail.ru\",\"login\":\"user3\",\"name\":\"User3\",\"birthday\":\"1985-05-11\"}]", friendsList);
        assertEquals("[{\"id\":4,\"friends\":{},\"email\":\"user3@mail.ru\",\"login\":\"user3\",\"name\":\"User3\",\"birthday\":\"1985-05-11\"}]", friendsList2);
        assertEquals("[{\"id\":4,\"friends\":{},\"email\":\"user3@mail.ru\",\"login\":\"user3\",\"name\":\"User3\",\"birthday\":\"1985-05-11\"}]", commonFriendsList);

    }
}
