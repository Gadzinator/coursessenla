package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.repository.AbstractDao;
import com.coursessenla.main.repository.MovieRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieRepositoryImpl extends AbstractDao<Movie, Long> implements MovieRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected MovieRepositoryImpl() {
		super(Movie.class);
	}

	@Override
	public List<Movie> findByGenre(String genreName) {
		return entityManager.createQuery(
						"SELECT m FROM Movie m JOIN FETCH  m.genres g WHERE g.name = :genreName", Movie.class)
				.setParameter("genreName", genreName)
				.getResultList();
	}
}
