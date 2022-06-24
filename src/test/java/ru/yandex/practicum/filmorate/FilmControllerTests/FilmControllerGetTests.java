package ru.yandex.practicum.filmorate.FilmControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.film.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FilmControllerGetTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getFilmTest() {

        Film film1 = new Film("Film1", "about 1", "1985-05-11", 2,null, null);
        Film film2 = new Film("Film2", "about 2", "2020-01-01", 1,null, null);

        HttpEntity<Film> request1 = new HttpEntity<>(film1);
        HttpEntity<Film> request2 = new HttpEntity<>(film2);

        //clearing map from previous tests
        restTemplate.delete("http://localhost:" + port + "/films");
        restTemplate.postForLocation("http://localhost:" + port + "/films", request1);
        restTemplate.postForLocation("http://localhost:" + port + "/films", request2);

        String mapFromServer = restTemplate.getForObject("http://localhost:" + port + "/films", String.class);
        String filmFromServer = restTemplate.getForObject("http://localhost:" + port + "/films/1", String.class);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity
                ("http://localhost:" + port + "/films/10",String.class);


        //check get all films
        assertEquals("[{\"id\":1,\"likes\":[],\"name\":\"Film1\",\"description\":\"about 1\",\"releaseDate\":\"1985-05-11\",\"duration\":2,\"genre\":null,\"mpa\":null},{\"id\":2,\"likes\":[],\"name\":\"Film2\",\"description\":\"about 2\",\"releaseDate\":\"2020-01-01\",\"duration\":1,\"genre\":null,\"mpa\":null}]", mapFromServer);
        //check get film by id
        assertEquals("{\"id\":1,\"likes\":[],\"name\":\"Film1\",\"description\":\"about 1\",\"releaseDate\":\"1985-05-11\",\"duration\":2,\"genre\":null,\"mpa\":null}", filmFromServer);
        //check get film by not existing id
        assertEquals("404 NOT_FOUND",responseEntity.getStatusCode().toString());

    }
}