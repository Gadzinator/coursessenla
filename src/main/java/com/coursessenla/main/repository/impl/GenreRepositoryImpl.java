package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.repository.AbstractDao;
import com.coursessenla.main.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GenreRepositoryImpl extends AbstractDao<Genre, Long> implements GenreRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected GenreRepositoryImpl() {
		super(Genre.class);
	}

	@Override
	public Optional<Genre> findByName(String name) {
		try {
			final Genre genre = entityManager.createQuery(
							"SELECT g FROM Genre g LEFT JOIN FETCH g.movies WHERE g.name = :name", Genre.class)
					.setParameter("name", name)
					.getSingleResult();

			return Optional.of(genre);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
