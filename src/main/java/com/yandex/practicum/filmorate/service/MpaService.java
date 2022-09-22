package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaService {
    private final MpaStorage mpaStorage;
    private int idGenerator = 0;

    public Mpa getMpa(int id) {
        Optional<Mpa> mpa = mpaStorage.get(id);
        if (mpa.isEmpty()) {
            throw new NotFoundException("Рейтинг с id = " + id + " не существует.");
        }
        return mpa.get();
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getMpa();
    }

    private int generatedId() {
        return ++idGenerator;
    }
}
