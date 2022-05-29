package ru.yandex.practicum.filmorate.FilmControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmLikesTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testLikes() {

        Film film0 = new Film("Film0", "about 0", "1985-05-11", 3);
        Film film1 = new Film("Film1", "about 1", "1985-05-11", 2);
        Film film2 = new Film("Film2", "about 2", "2020-01-01", 1);
        User user0 = new User
                ("user0@mail.ru", "user0", "User0", "1985-05-11");
        User user1 = new User
                ("user1@mail.ru", "user1", "User1", "1985-05-11");
        User user2 = new User
                ("user2@mail.ru", "user2", "User2", "1985-05-11");
        Film film3 = new Film("Film3", "about 3", "2020-01-01", 1);

        HttpEntity<Film> request0 = new HttpEntity<>(film0);
        HttpEntity<Film> request1 = new HttpEntity<>(film1);
        HttpEntity<Film> request2 = new HttpEntity<>(film2);
        HttpEntity<User> request3 = new HttpEntity<>(user0);
        HttpEntity<User> request4 = new HttpEntity<>(user1);
        HttpEntity<User> request5 = new HttpEntity<>(user2);
        HttpEntity<Film> request6 = new HttpEntity<>(film3);

        restTemplate.delete("http://localhost:" + port + "/films");
        restTemplate.delete("http://localhost:" + port + "/users");
        restTemplate.postForLocation("http://localhost:" + port + "/films", request0);
        restTemplate.postForLocation("http://localhost:" + port + "/films", request1);
        restTemplate.postForLocation("http://localhost:" + port + "/films", request2);
        restTemplate.postForLocation("http://localhost:" + port + "/users", request3);
        restTemplate.postForLocation("http://localhost:" + port + "/users", request4);
        restTemplate.postForLocation("http://localhost:" + port + "/users", request5);
        restTemplate.postForLocation("http://localhost:" + port + "/films", request6);

        //add likes
        restTemplate.put("http://localhost:" + port + "/films/1/like/1",null);
        restTemplate.put("http://localhost:" + port + "/films/2/like/1",null);
        restTemplate.put("http://localhost:" + port + "/films/2/like/2",null);
        restTemplate.put("http://localhost:" + port + "/films/2/like/3",null);
        restTemplate.put("http://localhost:" + port + "/films/3/like/1",null);
        restTemplate.put("http://localhost:" + port + "/films/3/like/3",null);

        //get list of films by number of likes desc order

        String listFromServer = restTemplate.getForObject(
                "http://localhost:" + port + "/films/popular",String.class);

        assertEquals("[{\"id\":2,\"likes\":[1,2,3],\"name\":\"Film1\",\"description\":\"about 1\",\"releaseDate\":\"1985-05-11\",\"duration\":2},{\"id\":3,\"likes\":[1,3],\"name\":\"Film2\",\"description\":\"about 2\",\"releaseDate\":\"2020-01-01\",\"duration\":1},{\"id\":1,\"likes\":[1],\"name\":\"Film0\",\"description\":\"about 0\",\"releaseDate\":\"1985-05-11\",\"duration\":3},{\"id\":4,\"likes\":[],\"name\":\"Film3\",\"description\":\"about 3\",\"releaseDate\":\"2020-01-01\",\"duration\":1}]",listFromServer);

        //delete 1 like
        restTemplate.delete("http://localhost:" + port + "/films/2/like/1");
        String filmFromServer = restTemplate.getForObject("http://localhost:" + port + "/films/2",String.class);
        assertEquals("{\"id\":2,\"likes\":[2,3],\"name\":\"Film1\",\"description\":\"about 1\",\"releaseDate\":\"1985-05-11\",\"duration\":2}",filmFromServer);

        //add like to not existing film
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/films/10/like/1",
                HttpMethod.PUT,null,String.class);
        assertEquals("404 NOT_FOUND",responseEntity.getStatusCode().toString());

        //add like by not existing user
        ResponseEntity<String> responseEntity1 = restTemplate.exchange(
                "http://localhost:" + port + "/films/1/like/10",
                HttpMethod.PUT,null,String.class);
        assertEquals("404 NOT_FOUND",responseEntity1.getStatusCode().toString());

        //delete like to not existing film
        ResponseEntity<String> responseEntity2 = restTemplate.exchange(
                "http://localhost:" + port + "/films/10/like/1",
                HttpMethod.DELETE,null,String.class);
        assertEquals("404 NOT_FOUND",responseEntity2.getStatusCode().toString());

        //delete like by not existing user
        ResponseEntity<String> responseEntity3 = restTemplate.exchange(
                "http://localhost:" + port + "/films/1/like/10",
                HttpMethod.DELETE,null,String.class);
        assertEquals("404 NOT_FOUND",responseEntity3.getStatusCode().toString());

    }
    }

