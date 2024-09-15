package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.repository.AbstractDao;
import com.coursessenla.main.repository.UserRepository;
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
public class UserRepositoryImpl extends AbstractDao<User, Long> implements UserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected UserRepositoryImpl() {
		super(User.class);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		final EntityGraph<User> entityGraph = entityManager.createEntityGraph(User.class);
		entityGraph.addAttributeNodes("roles");
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		final Root<User> root = criteriaQuery.from(User.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("email"), email));

		final TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);

		try {
			User user = typedQuery.getSingleResult();
			return Optional.ofNullable(user);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
