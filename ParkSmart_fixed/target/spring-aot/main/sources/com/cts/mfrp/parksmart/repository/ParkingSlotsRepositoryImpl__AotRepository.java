package com.cts.mfrp.parksmart.repository;

import com.cts.mfrp.parksmart.model.ParkingSlots;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.Param;

/**
 * AOT generated JPA repository implementation for {@link ParkingSlotsRepository}.
 */
@Generated
public class ParkingSlotsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ParkingSlotsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ParkingSlotsRepository#findBySpaceId(java.lang.Integer)}.
   */
  public List<ParkingSlots> findBySpaceId(@Param("spaceId") Integer spaceId) {
    String queryString = "SELECT s FROM ParkingSlots s WHERE s.parkingSpace.spaceId = :spaceId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("spaceId", spaceId);

    return (List<ParkingSlots>) query.getResultList();
  }
}
