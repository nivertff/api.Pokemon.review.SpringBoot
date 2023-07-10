package com.pokemonReview.api.service.Imp;

import com.pokemonReview.api.Dto.PokemonResponse;
import com.pokemonReview.api.Dto.ReviewDto;
import com.pokemonReview.api.exceptions.PokemonNotFound;
import com.pokemonReview.api.exceptions.ReviewNotFound;
import com.pokemonReview.api.models.Pokemon;
import com.pokemonReview.api.models.Review;
import com.pokemonReview.api.repository.PokemonRepository;
import com.pokemonReview.api.repository.ReviewRepository;
import com.pokemonReview.api.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImp  implements ReviewService {
    private ReviewRepository reviewRepository;
    private PokemonRepository pokemonRepository;


    public ReviewServiceImp(ReviewRepository reviewRepository, PokemonRepository pokemonRepository) {
        this.reviewRepository = reviewRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public ReviewDto createReview(Long pokemonId, ReviewDto reviewDto) {
        Review review = mapToEntiry(reviewDto);
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFound("not fount"));

        review.setPokemon(pokemon);
        Review newReview = reviewRepository.save(review);

        return mapToDto(newReview);
    }

    private Review mapToEntiry(ReviewDto reviewDto) {
        return Review.builder()
                .id(reviewDto.getId())
                .title(reviewDto.getTitle())
                .content(reviewDto.getContent())
                .stars(reviewDto.getStars())
                .build();
    }

    private ReviewDto mapToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .stars(review.getStars())
                .build();
    }

    @Override
    public List<ReviewDto> getReviewsByPokemonId(Long id) {
        List<Review> reviews = reviewRepository.findByPokemonId(id);

        return reviews.stream().map(review -> mapToDto(review)).collect(Collectors.toList());
    }

    @Override
    public ReviewDto getReviewById(Long pokemonId, Long reviewId) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFound("not fount"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFound("not fount"));

        if (review.getPokemon().getId() != pokemon.getId()) {
            throw new ReviewNotFound("not found");
        }
        return mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(Long pokemonId, Long reviewId, ReviewDto reviewDto) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFound("Pokemon with associated review not found"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFound("Review with associate pokemon not found"));

        if(review.getPokemon().getId() != pokemon.getId()) {
            throw new ReviewNotFound("This review does not belong to a pokemon");
        }

        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setStars(reviewDto.getStars());

        Review updateReview = reviewRepository.save(review);

        return mapToDto(updateReview);
    }

    @Override
    public void deleteReview(Long pokemonId, Long reviewId) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFound("Pokemon with associated review not found"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFound("Review with associate pokemon not found"));

        if(review.getPokemon().getId() != pokemon.getId()) {
            throw new ReviewNotFound("This review does not belong to a pokemon");
        }

        reviewRepository.delete(review);
    }


}
