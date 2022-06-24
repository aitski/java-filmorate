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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FilmControllerPostTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createFilmTest() {

        Film film = new Film
                ("Film1", "about 1", "1985-05-11", 2,null, null);
        HttpEntity<Film> request = new HttpEntity<>(film);
        //clearing map from previous tests
        restTemplate.delete("http://localhost:" + port + "/films");
        Film filmFromServer = restTemplate.postForObject
                ("http://localhost:" + port + "/films", request, Film.class);
        film.setId(filmFromServer.getId());
        assertNotNull(filmFromServer);
        assertEquals(film, filmFromServer);

    }

    @Test
    public void blankFilmNameTest() {

        Film film = new Film
                ("", "about 1", "1985-05-11", 2,null, null);
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<Film> responseEntity = restTemplate.postForEntity
                ("http://localhost:" + port + "/films",request,Film.class);
        assertEquals("400 BAD_REQUEST",responseEntity.getStatusCode().toString());
    }

    @Test
    public void tooLongDescriptionTest() {

        Film film = new Film
                ("Film1", "abouttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt",
                        "1985-05-11", 2,null, null);
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<Film> responseEntity = restTemplate.postForEntity
                ("http://localhost:" + port + "/films",request,Film.class);
        assertEquals("400 BAD_REQUEST",responseEntity.getStatusCode().toString());
    }

    @Test
    public void tooOldReleaseDateTest() {

        Film film = new Film
                ("Film1", "about 1", "1565-05-11", 2,null, null);
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<Film> responseEntity = restTemplate.postForEntity
                ("http://localhost:" + port + "/films", request, Film.class);
        assertEquals("400 BAD_REQUEST",responseEntity.getStatusCode().toString());
    }

    @Test
    public void negativeDurationTest() {

        Film film = new Film
                ("Film1", "about 1", "1565-05-11", -2,null, null);
        HttpEntity<Film> request = new HttpEntity<>(film);
        ResponseEntity<Film> responseEntity = restTemplate.postForEntity
                ("http://localhost:" + port + "/films",request,Film.class);
        assertEquals("400 BAD_REQUEST",responseEntity.getStatusCode().toString());
    }
    }