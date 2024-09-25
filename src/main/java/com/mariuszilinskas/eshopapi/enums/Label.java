package com.mariuszilinskas.eshopapi.enums;

public enum Label {
    DRINK, FOOD, CLOTHES, LIMITED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
