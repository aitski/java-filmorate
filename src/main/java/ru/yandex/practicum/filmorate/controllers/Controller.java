package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public abstract class Controller<T> {


    @PostMapping
    public abstract T save(@RequestBody T t);

    @PutMapping
    public abstract T update(@RequestBody T t);

    @GetMapping
    public abstract List<T> findAll();
}


