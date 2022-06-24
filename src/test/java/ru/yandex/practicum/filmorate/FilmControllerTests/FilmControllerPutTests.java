package ru.yandex.practicum.filmorate.FilmControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import ru.yandex.practicum.filmorate.model.film.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FilmControllerPutTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void updateFilmTest() {

        restTemplate.delete("http://localhost:" + port + "/films");
        Film film = new Film
                ("Film1", "about 1", "1985-05-11", 2,null, null);
        HttpEntity<Film> request = new HttpEntity<>(film);
        //clearing map from previous tests
        restTemplate.postForLocation("http://localhost:" + port + "/films",request);

        film.setDuration(3);
        film.setId(1);

        HttpEntity<Film> request2 = new HttpEntity<>(film);

        restTemplate.put("http://localhost:" + port + "/films", request2);
        String mapFromServer = restTemplate.getForObject
                ("http://localhost:" + port + "/films",String.class);

        assertEquals("[{\"id\":1,\"likes\":[],\"name\":\"Film1\",\"description\":\"about 1\",\"releaseDate\":\"1985-05-11\",\"duration\":3,\"genre\":null,\"mpa\":null}]",
                mapFromServer);
    }
    }