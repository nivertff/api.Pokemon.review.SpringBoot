package com.pokemonReview.api.service.Imp;

import com.pokemonReview.api.Dto.PokemonDto;
import com.pokemonReview.api.Dto.PokemonResponse;
import com.pokemonReview.api.exceptions.PokemonNotFound;
import com.pokemonReview.api.models.Pokemon;
import com.pokemonReview.api.repository.PokemonRepository;
import com.pokemonReview.api.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PageRanges;
import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonServiceImp  implements PokemonService {
    PokemonRepository pokemonRepository;

    @Autowired
    public PokemonServiceImp(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public PokemonDto createPokemon(PokemonDto pokemonDto) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(pokemonDto.getName());
        pokemon.setType(pokemonDto.getType());

        Pokemon newPokemon = pokemonRepository.save(pokemon);

        PokemonDto pokemonDto1 = new PokemonDto();
        pokemonDto1.setId(newPokemon.getId());
        pokemonDto1.setName(newPokemon.getName());
        pokemonDto1.setType(newPokemon.getType());
        return pokemonDto1;
    }

    @Override
    public PokemonResponse getAllPokemon(int pageNo,int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo,pageSize);
        Page<Pokemon> pokemons = pokemonRepository.findAll(pageable);
        List<Pokemon> list = pokemons.getContent();
        List<PokemonDto> pokemonDtos = list.stream().map(pokemon -> mapToPokemonDto(pokemon)).collect(Collectors.toList());


        PokemonResponse pokemonResponse = new PokemonResponse();
        pokemonResponse.setContent(pokemonDtos);
        pokemonResponse.setPageNo(pokemons.getNumber());
        pokemonResponse.setPageSize(pokemons.getSize());
        pokemonResponse.setTotalElements((pokemons.getTotalElements()));
        pokemonResponse.setTotalPages(pokemons.getTotalPages());
        pokemonResponse.setLast(pokemons.isLast());


        return pokemonResponse;
    }

    @Override
    public PokemonDto getPokemonById(Long id) {
        Pokemon pokemon = pokemonRepository.findById(id).orElseThrow(()->new PokemonNotFound("Pokemon Not Found"));
        return mapToPokemonDto(pokemon);
    }

    @Override
    public PokemonDto updatePokemon(PokemonDto pokemonDto, Long id) {
        Pokemon pokemon = pokemonRepository.findById(id).orElseThrow(()->new PokemonNotFound("Pokemon Not Found"));
        pokemon.setType(pokemonDto.getType());
        pokemon.setName(pokemonDto.getName());

        Pokemon pokemon1 = pokemonRepository.save(pokemon);
        return mapToPokemonDto(pokemon1);

    }

    @Override
    public void deletePokemon(Long id) {
        Pokemon pokemon = pokemonRepository.findById(id).orElseThrow(()->new PokemonNotFound("Pokemon Not Found"));
        pokemonRepository.delete(pokemon);
    }

    private PokemonDto mapToPokemonDto(Pokemon pokemon){
        return PokemonDto.builder()
                .id(pokemon.getId())
                .name(pokemon.getName())
                .type(pokemon.getType())
                .build();
    }
    private Pokemon mapToPokemon(PokemonDto pokemonDto){
        return Pokemon.builder()
                .id(pokemonDto.getId())
                .name(pokemonDto.getName())
                .type(pokemonDto.getType())
                .build();
    }


}
