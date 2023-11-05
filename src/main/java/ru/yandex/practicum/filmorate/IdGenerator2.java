package ru.yandex.practicum.filmorate;

public class IdGenerator2 {
    private Integer currentValue;

    public IdGenerator2() {
        this.currentValue = 0;
    }

    public int generateId() {
        return currentValue++;
    }
}