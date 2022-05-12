package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.Validation;

import javax.validation.Valid;
import java.util.Map;

public abstract class Controller<T> {


    @PostMapping
    public abstract T save(@Valid @RequestBody T t) throws Validation;

    @PutMapping
    public abstract T update(@Valid @RequestBody T t) throws Validation;

    @DeleteMapping
    public abstract void deleteAll();

    @GetMapping
    public abstract Map<Integer, T> findAll();
}


