package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.MpaService;
import com.yandex.practicum.filmorate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaController {
    private final MpaService mpaService;

    @GetMapping()
    public List<Mpa> getAll() {
        return mpaService.getAllMpa();
    }


    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        return mpaService.getMpa(id);
    }
}
