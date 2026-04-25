package com.cts.mfrp.parksmart.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.Param;

/**
 * AOT generated JPA repository implementation for {@link ReviewsRepository}.
 */
@Generated
public class ReviewsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ReviewsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ReviewsRepository#existsByBookingId(java.lang.Integer)}.
   */
  public boolean existsByBookingId(@Param("bookingId") Integer bookingId) {
    String queryString = "SELECT COUNT(*) > 0 FROM reviews WHERE booking_id = :bookingId";
    Query query = this.entityManager.createNativeQuery(queryString);
    query.setParameter("bookingId", bookingId);

    return (Boolean) convertOne(query.getSingleResultOrNull(), true, Boolean.class);
  }
}
