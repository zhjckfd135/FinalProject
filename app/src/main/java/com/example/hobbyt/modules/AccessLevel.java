package com.example.hobbyt.modules;

public enum AccessLevel{
    READ(1),
    WRITE_AND_READ(2);

    private final int id;
    AccessLevel(int id) { this.id = id; }
    public int getValue() { return id; }
    static public AccessLevel getEnum(int value) {
        if(value == 1) return READ;
        if(value == 2) return WRITE_AND_READ;

        throw new NullPointerException();
    }
}
