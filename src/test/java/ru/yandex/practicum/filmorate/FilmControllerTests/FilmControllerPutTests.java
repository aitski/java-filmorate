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
public class FilmControllerPutTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void updateFilmTest() {

        Film film = new Film
                ("Film1", "about 1", "1985-05-11", 2);
        HttpEntity<Film> request = new HttpEntity<>(film);
        //clearing map from previous tests
        restTemplate.delete("http://localhost:" + port + "/films");
        restTemplate.postForLocation("http://localhost:" + port + "/films",request);

        film.setDuration(3);

        restTemplate.put("http://localhost:" + port + "/films", request);
        String mapFromServer = restTemplate.getForObject
                ("http://localhost:" + port + "/films",String.class);

        assertEquals("{\"0\":{\"id\":0,\"name\":\"Film1\",\"description\":\"about 1\",\"releaseDate\":\"1985-05-11\",\"duration\":3}}",
                mapFromServer);
    }
    }