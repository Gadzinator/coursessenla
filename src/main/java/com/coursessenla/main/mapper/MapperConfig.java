package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public GenericMapperImpl<Actor, ActorDto> actorMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, Actor.class, ActorDto.class);
	}

	@Bean
	public GenericMapperImpl<Director, DirectorDto> directorMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, Director.class, DirectorDto.class);
	}

	@Bean
	public GenericMapperImpl<Genre, GenreDto> genreMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, Genre.class, GenreDto.class);
	}

	@Bean
	public GenericMapperImpl<User, UserDto> userMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, User.class, UserDto.class);
	}

	@Bean
	public GenericMapperImpl<User, RegistrationUserDto> registrationUserMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, User.class, RegistrationUserDto.class);
	}

	@Bean
	public GenericMapperImpl<Movie, MovieDto> movieMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, Movie.class, MovieDto.class);
	}

	@Bean
	public GenericMapperImpl<Playlist, PlayListDto> playlistMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, Playlist.class, PlayListDto.class);
	}

	@Bean
	public GenericMapperImpl<Review, ReviewDto> reviewMapper(ModelMapper modelMapper) {
		return new GenericMapperImpl<>(modelMapper, Review.class, ReviewDto.class);
	}
}
