package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase

class FilmorateApplicationTests {
	private final UserDbStorage userStorage;

	@Autowired
	FilmorateApplicationTests(UserDbStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Test
	public void testFindUserById() {

		userStorage.save(new User("sfafs@gss.ru","gvdsg","sdgsgd","1985-05-11"));
		User user = userStorage.getById(1L);

		assertThat(user)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id",1L)
				.hasFieldOrPropertyWithValue("login","gvdsg");
	}
}