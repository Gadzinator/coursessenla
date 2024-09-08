package com.coursessenla.main.repository.impl;

import com.coursessenla.main.config.ConnectionHolder;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

	private static final String INSERT_REVIEW = "INSERT INTO review (id, content, rating, user_id, movie_id) VALUES (?, ?, ?, ?, ?)";
	private static final String SELECT_FIND_REVIEW_BY_ID = "SELECT * FROM review WHERE id = ?";
	private static final String SELECT_FIND_USER_BY_ID = "SELECT * FROM user WHERE id = ?";
	private static final String SELECT_FIND_MOVIE_BY_ID = "SELECT * FROM movie WHERE id = ?";
	private static final String SELECT_FIND_GENRE_ID_BY_MOVIE_ID = "SELECT genre_id FROM movie_genre WHERE movie_id = ?";
	private static final String UPDATE_REVIEW = "UPDATE review SET content = ?, rating = ?, user_id = ?, movie_id = ? WHERE id = ?";
	private static final String DELETE_REVIEW_BY_ID = "DELETE FROM review WHERE id = ?";
	private final ConnectionHolder connectionHolder;

	@Override
	public void save(Review review) {
		try {
			final Connection connection = connectionHolder.getConnection();

			try (final PreparedStatement statement = connection.prepareStatement(INSERT_REVIEW)) {
				statement.setLong(1, review.getId());
				statement.setString(2, review.getContent());
				statement.setDouble(3, review.getRating());

				final User user = review.getUser();
				statement.setLong(4, user.getId());

				final Movie movie = review.getMovie();
				statement.setLong(5, movie.getId());
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<Review> findById(long id) {
		try {
			Connection connection = connectionHolder.getConnection();

			try (final PreparedStatement statement = connection.prepareStatement(SELECT_FIND_REVIEW_BY_ID)) {
				statement.setLong(1, id);

				Review review = new Review();
				try (ResultSet resultSet = statement.executeQuery();) {
					if (resultSet.next()) {
						review.setId(resultSet.getLong("id"));
						review.setContent(resultSet.getString("content"));
						review.setRating(resultSet.getDouble("rating"));

						final long userId = resultSet.getLong("user_id");
						final User user = findUserById(userId);
						review.setUser(user);

						final long movieId = resultSet.getLong("movie_id");
						final Movie movie = findMovieById(movieId);
						review.setMovie(movie);
					}

					return Optional.of(review);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateById(long id, Review reviewUpdate) {
		try {
			final Connection connection = connectionHolder.getConnection();
			try (final PreparedStatement statement = connection.prepareStatement(UPDATE_REVIEW)) {
				statement.setString(1, reviewUpdate.getContent());
				statement.setDouble(2, reviewUpdate.getRating());
				statement.setLong(3, reviewUpdate.getUser().getId());
				statement.setLong(4, reviewUpdate.getMovie().getId());
				statement.setLong(5, id);

				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(long id) {
		try {
			final Connection connection = connectionHolder.getConnection();
			try (final PreparedStatement statement = connection.prepareStatement(DELETE_REVIEW_BY_ID)) {
				statement.setLong(1, id);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	private User findUserById(long userId) throws SQLException {
		User user = new User();
		Connection connection = connectionHolder.getConnection();
		try (final PreparedStatement statement = connection.prepareStatement(SELECT_FIND_USER_BY_ID)) {
			statement.setLong(1, userId);

			try (final ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					user.setId(resultSet.getLong("id"));
					user.setEmail(resultSet.getString("email"));
					user.setPassword(resultSet.getString("password"));
				} else {
					throw new IllegalArgumentException("User with this id " + userId + " was not found");
				}
			}
		}

		return user;
	}

	private Movie findMovieById(long movieId) throws SQLException {
		Movie movie = new Movie();
		final Connection connection = connectionHolder.getConnection();
		try (final PreparedStatement statement = connection.prepareStatement(SELECT_FIND_MOVIE_BY_ID)) {
			statement.setLong(1, movieId);

			try (final ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					movie.setId(resultSet.getLong("id"));
					movie.setTitle(resultSet.getString("title"));
					movie.setDescription(resultSet.getString("description"));
					movie.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());

					final List<Genre> genres = findGenresById(findGenreIdByMovieId(movieId));
					movie.setGenres(genres);
					movie.setRating(resultSet.getDouble("rating"));
				}
			}
		}

		return movie;
	}

	private List<Long> findGenreIdByMovieId(long movieId) throws SQLException {
		List<Long> genresId = new ArrayList<>();
		final Connection connection = connectionHolder.getConnection();
		try (final PreparedStatement statement = connection.prepareStatement(SELECT_FIND_GENRE_ID_BY_MOVIE_ID)) {
			statement.setLong(1, movieId);
			try (final ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					final long genreId = resultSet.getLong("genre_id");
					genresId.add(genreId);
				}
			}
		}

		return genresId;
	}

	private List<Genre> findGenresById(List<Long> genresId) throws SQLException {
		List<Genre> genres = new ArrayList<>();
		final String sqlQuery = buildSqlQuery(genresId.size());
		final Connection connection = connectionHolder.getConnection();
		try (final PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
			for (int i = 0; i < genresId.size(); i++) {
				statement.setLong(i + 1, genresId.get(i));
			}
			try (final ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Genre genre = new Genre();
					genre.setId(resultSet.getLong("id"));
					genre.setName(resultSet.getString("name"));
					genres.add(genre);
				}
			}
		}

		return genres;
	}

	private String buildSqlQuery(int count) {
		StringBuilder sb = new StringBuilder("SELECT * FROM genre WHERE id IN(");
		for (int i = 0; i < count; i++) {
			sb.append("?");
			if (i < count - 1) {
				sb.append(",");
			}
		}
		sb.append(")");

		return sb.toString();
	}
}
