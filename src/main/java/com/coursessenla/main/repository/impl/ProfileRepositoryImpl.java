package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.Profile_;
import com.coursessenla.main.repository.AbstractDao;
import com.coursessenla.main.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProfileRepositoryImpl extends AbstractDao<Profile, Long> implements ProfileRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected ProfileRepositoryImpl() {
		super(Profile.class);
	}

	@Override
	public Optional<Profile> findByName(String name) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Profile> criteriaQuery = criteriaBuilder.createQuery(Profile.class);
		final Root<Profile> root = criteriaQuery.from(Profile.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(Profile_.firstName), name));

		try {
			final Profile result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
