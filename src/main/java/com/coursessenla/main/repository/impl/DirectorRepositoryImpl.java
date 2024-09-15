package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.repository.AbstractDao;
import com.coursessenla.main.repository.DirectorRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DirectorRepositoryImpl extends AbstractDao<Director, Long> implements DirectorRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected DirectorRepositoryImpl() {
		super(Director.class);
	}

	@Override
	public Optional<Director> findByName(String name) {
		final EntityGraph<Director> entityGraph = entityManager.createEntityGraph(Director.class);
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Director> criteriaQuery = criteriaBuilder.createQuery(Director.class);
		final Root<Director> root = criteriaQuery.from(Director.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));

		final TypedQuery<Director> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);

		try {
			final Director director = typedQuery.getSingleResult();
			return Optional.ofNullable(director);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
