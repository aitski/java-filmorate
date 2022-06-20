package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public abstract class Controller<T> {


    @PostMapping
    public abstract void save(@RequestBody T t);

    @PutMapping
    public abstract T update(@RequestBody T t);

    @DeleteMapping
    public abstract void deleteAll();

    @GetMapping
    public abstract List<T> findAll();
}


