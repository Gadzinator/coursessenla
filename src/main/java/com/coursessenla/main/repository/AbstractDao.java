package com.coursessenla.main.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


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

	public Page<T> findAll(Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);

		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);

		if (pageable != null) {
			typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			typedQuery.setMaxResults(pageable.getPageSize());

			List<T> resultList = typedQuery.getResultList();
			long totalCount = countTotal();

			return new PageImpl<>(resultList, pageable, totalCount);
		} else {
			return new PageImpl<>(typedQuery.getResultList());
		}
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

	private long countTotal() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = countQuery.from(entityClass);
		countQuery.select(criteriaBuilder.count(root));

		TypedQuery<Long> typedQuery = entityManager.createQuery(countQuery);

		return typedQuery.getSingleResult();
	}
}
