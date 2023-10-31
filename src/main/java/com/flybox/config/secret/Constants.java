package com.flybox.config.secret;

import com.flybox.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public abstract class Constants {

    public static final String API_KEY = "SmF2YSBEZXZlbG9wZXIgZnJvbSBJVE1PIQ==";

    public static void validateKey(String apiKey) {

        if (!apiKey.equals(API_KEY)) {
            throw new CustomException("Неверный ключ", HttpStatus.FORBIDDEN);
        }
    }

}
