CREATE TABLE IF NOT EXISTS users
(
    user_id       int auto_increment,
    user_email    varchar(20) not null,
    user_login    varchar(20) not null,
    user_name     varchar(50),
    user_birthday date,
    constraint users_pk
        primary key (user_id),
    constraint future_birthday
        CHECK (user_birthday <= CURRENT_DATE),
    constraint email_format
        CHECK (user_email LIKE '%_@_%._%'),
    constraint login_spaces
        CHECK (user_login NOT LIKE '% %')
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id   int,
    genre_name varchar(20),
    constraint genre_pk
        primary key (genre_id)
);

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   int,
    mpa_name varchar(20),
    constraint mpa_pk
        primary key (mpa_id)
);

CREATE TABLE IF NOT EXISTS films
(
    film_id           int auto_increment,
    film_description  varchar(200),
    film_name         varchar(50) not null,
    film_release_date date,
    film_duration     int,
    genre_id          int,
    mpa_id            int,
    constraint films_pk
        primary key (film_id),
    constraint genre_fk
        foreign key (genre_id)
            references genres (genre_id),
    constraint mpa_fk
        foreign key (mpa_id)
            references mpa (mpa_id),
    constraint positive_duration
        CHECK (film_duration > 0),
    constraint film_date_check
        CHECK (film_release_date >= '1895-12-28')
);

CREATE TABLE IF NOT EXISTS likes
(
    like_id int auto_increment,
    user_id int,
    film_id int,
    constraint like_pk
        primary key (like_id),
    constraint user_fk
        foreign key (user_id)
            references users (user_id),
    constraint film_fk
        foreign key (film_id)
            references films (film_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    friends_id int auto_increment,
    user_id    int,
    friend_id  int,
    status     varchar(20) default 'PENDING',
    constraint friend_pk
        primary key (friends_id),
    constraint user_id_fk
        foreign key (user_id)
            references users (user_id),
    constraint friend_id_fk
        foreign key (friend_id)
            references users (user_id)
);