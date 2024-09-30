package com.coursessenla.main.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	@Column(name = "release_date")
	private LocalDate releaseDate;

	@ManyToMany
	@JoinTable(
			name = "movie_genre",
			joinColumns = @JoinColumn(name = "movie_id"),
			inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private List<Genre> genres;

	private Double rating;

	@ManyToMany
	@JoinTable(
			name = "movie_director",
			joinColumns = @JoinColumn(name = "movie_id"),
			inverseJoinColumns = @JoinColumn(name = "director_id")
	)
	private List<Director> directors;

	@OneToMany(mappedBy = "movie")
	private List<CharacterInfo> movieActors;

	@ManyToMany
	@JoinTable(
			name = "playlist_movie",
			joinColumns = @JoinColumn(name = "movie_id"),
			inverseJoinColumns = @JoinColumn(name = "playlist_id")
	)
	private List<Playlist> playlists;

	@OneToMany(mappedBy = "movie")
	private List<Review> reviews;

	@Override
	public String toString() {
		return "Movie{" +
				"id=" + id +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", releaseDate=" + releaseDate +
				", genres=" + genres +
				", rating=" + rating +
				", directors=" + directors +
				", movieActors=" + movieActors +
				", playlists=" + playlists +
				", reviews=" + reviews +
				'}';
	}
}
