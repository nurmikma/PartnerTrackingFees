package com.evocon.partnertracking.utils;

public class Validation {

    public static boolean isValidId(String id) {
        return id != null && id.matches("\\d+");
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z ]+") && !name.trim().isEmpty();
    }
}
