package com.pokemonReview.api.service;

import com.pokemonReview.api.Dto.PokemonDto;
import com.pokemonReview.api.Dto.PokemonResponse;
import com.pokemonReview.api.models.Pokemon;

import java.util.List;

public interface PokemonService {
    PokemonDto createPokemon(PokemonDto pokemonDto);
    PokemonResponse getAllPokemon(int pageNo, int pageSize);
    PokemonDto getPokemonById(Long id);

    PokemonDto updatePokemon(PokemonDto pokemonDto, Long id);
    void deletePokemon(Long id);
}
