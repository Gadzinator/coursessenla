package com.coursessenla.main.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDao<T, PK extends Serializable> {

	@PersistenceContext
	private EntityManager entityManager;
	private final Class<T> entityClass;

	public void save(T entity) {
		log.info("Starting method save with entity: {}", entity);

		entityManager.persist(entity);
		log.info("Ending method save. Entity persisted: {}", entity);

	}

	public Optional<T> findById(PK id) {
		log.info("Starting method findById with id: {}", id);

		final T entity = entityManager.find(entityClass, id);

		log.info("Ending method findById. Entity found: {}", entity);

		return Optional.ofNullable(entity);
	}

	public List<T> findAll() {
		log.info("Starting method findAll");

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		final Root<T> root = criteriaQuery.from(entityClass);

		criteriaQuery.select(root);
		final List<T> resultList = entityManager.createQuery(criteriaQuery).getResultList();

		log.info("Ending method findAll. Entities found: {}", resultList);

		return resultList;
	}

	public void update(T entity) {
		log.info("Starting method update with entity: {}", entity);

		entityManager.merge(entity);

		log.info("Ending method update. Entity updated: {}", entity);
	}

	public void deleteById(PK id) {
		log.info("Starting method deleteById with id: {}", id);

		T entityToRemove = entityManager.find(entityClass, id);
		if (entityToRemove != null) {
			entityManager.remove(entityToRemove);
			log.info("Ending method deleteById. Entity with id {} removed.", id);
		} else {
			log.debug("Ending method deleteById. No entity found with id {}.", id);
		}
	}
}
