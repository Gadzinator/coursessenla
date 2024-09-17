package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.domain.entity.Actor_;
import com.coursessenla.main.repository.AbstractDao;
import com.coursessenla.main.repository.ActorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ActorRepositoryImpl extends AbstractDao<Actor, Long> implements ActorRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected ActorRepositoryImpl() {
		super(Actor.class);
	}

	@Override
	public Optional<Actor> findByName(String name) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
		final Root<Actor> root = criteriaQuery.from(Actor.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(Actor_.name), name));

		try {
			final Actor result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
