package com.pokemonReview.api.exceptions;

public class ReviewNotFound extends RuntimeException {
    private static final long serialVerisionUID = 2;
    public ReviewNotFound(String message) {
        super(message);
    }
}
