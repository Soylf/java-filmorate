package ru.yandex.practicum.filmorate;

public class IdGenerator {
    private Integer currentValue;

    public IdGenerator() {
        this.currentValue = 1;
    }

    public int generateId() {
        return currentValue++;
    }
}