package com.cts.mfrp.parksmart.repository;

import com.cts.mfrp.parksmart.model.PromoCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link PromoCodeRepository}.
 */
@Generated
public class PromoCodeRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public PromoCodeRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link PromoCodeRepository#findByCode(java.lang.String)}.
   */
  public Optional<PromoCode> findByCode(String code) {
    String queryString = "SELECT p FROM PromoCode p WHERE p.code = :code";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("code", code);

    return Optional.ofNullable((PromoCode) convertOne(query.getSingleResultOrNull(), false, PromoCode.class));
  }
}
