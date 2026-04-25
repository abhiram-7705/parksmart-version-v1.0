package com.cts.mfrp.parksmart.repository;

import com.cts.mfrp.parksmart.model.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link UserRepository}.
 */
@Generated
public class UserRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public UserRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByEmail(java.lang.String)}.
   */
  public Optional<Users> findByEmail(String email) {
    String queryString = "SELECT u FROM Users u WHERE u.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);

    return Optional.ofNullable((Users) convertOne(query.getSingleResultOrNull(), false, Users.class));
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByResetToken(java.lang.String)}.
   */
  public Optional<Users> findByResetToken(String token) {
    String queryString = "SELECT u FROM Users u WHERE u.resetToken = :token";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("token", token);

    return Optional.ofNullable((Users) convertOne(query.getSingleResultOrNull(), false, Users.class));
  }
}
