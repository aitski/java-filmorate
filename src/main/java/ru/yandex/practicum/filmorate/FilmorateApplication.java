package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
/*
        User user1 = new User( "user@mail.ru", "userlogin", "Leonid", LocalDate.of(1985, 05, 11));
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd")
                .create();

        String json = gson.toJson(user1);
        System.out.println(json);
        System.out.println(gson.fromJson(json, User.class));
*/
    }

}
