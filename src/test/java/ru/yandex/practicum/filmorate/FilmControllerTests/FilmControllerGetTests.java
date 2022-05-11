package ru.yandex.practicum.filmorate.FilmControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FilmControllerGetTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getFilmTest() {

        Film Film1 = new Film
                ("Film1", "about 1", "1985-05-11", 2);
        Film Film2 = new Film
                ("Film2", "about 2", "2020-01-01", 1);

        HttpEntity<Film> request1 = new HttpEntity<>(Film1);
        HttpEntity<Film> request2 = new HttpEntity<>(Film2);
        //clearing map from previous tests
        restTemplate.delete("http://localhost:" + port + "/films");
        restTemplate.postForLocation("http://localhost:" + port + "/films", request1);
        restTemplate.postForLocation("http://localhost:" + port + "/films", request2);

        String mapFromServer = restTemplate.getForObject
                ("http://localhost:" + port + "/films", String.class);

        assertEquals("{\"0\":{\"id\":0,\"name\":\"Film1\",\"description\":\"about 1\",\"releaseDate\":\"1985-05-11\",\"duration\":2},\"1\":{\"id\":1,\"name\":\"Film2\",\"description\":\"about 2\",\"releaseDate\":\"2020-01-01\",\"duration\":1}}",
                mapFromServer);
    }
}