package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.Role;
import com.coursessenla.main.domain.entity.Role_;
import com.coursessenla.main.repository.AbstractDao;
import com.coursessenla.main.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends AbstractDao<Role, Long> implements RoleRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected RoleRepositoryImpl() {
		super(Role.class);
	}

	@Override
	public Optional<Role> findByName(String name) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
		final Root<Role> root = criteriaQuery.from(Role.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(Role_.name), name));

		try {
			final Role result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
