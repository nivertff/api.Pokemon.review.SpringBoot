package com.pokemonReview.api.exceptions;

public class PokemonNotFound  extends RuntimeException{
    private static final long serialVersionUID = 1;
    public PokemonNotFound(String message){
        super(message);
    }
}
