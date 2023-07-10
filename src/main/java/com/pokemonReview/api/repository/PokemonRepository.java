package com.pokemonReview.api.repository;

import com.pokemonReview.api.models.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<Pokemon,Long> {

}
