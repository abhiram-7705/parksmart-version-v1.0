package com.cts.mfrp.parksmart.repository;

import com.cts.mfrp.parksmart.model.ParkingSpaces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.Param;

/**
 * AOT generated JPA repository implementation for {@link ParkingSpacesRepository}.
 */
@Generated
public class ParkingSpacesRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ParkingSpacesRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ParkingSpacesRepository#findByOwnerUserId(int)}.
   */
  public List<ParkingSpaces> findByOwnerUserId(int userId) {
    String queryString = "SELECT p FROM ParkingSpaces p WHERE p.owner.userId = :userId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("userId", userId);

    return (List<ParkingSpaces>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ParkingSpacesRepository#findBySpaceIdAndOwnerUserId(java.lang.Integer,int)}.
   */
  public Optional<ParkingSpaces> findBySpaceIdAndOwnerUserId(Integer spaceId, int userId) {
    String queryString = "SELECT p FROM ParkingSpaces p WHERE p.spaceId = :spaceId AND p.owner.userId = :userId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("spaceId", spaceId);
    query.setParameter("userId", userId);

    return Optional.ofNullable((ParkingSpaces) convertOne(query.getSingleResultOrNull(), false, ParkingSpaces.class));
  }

  /**
   * AOT generated implementation of {@link ParkingSpacesRepository#findFirstByLocationIgnoreCaseAndCityIgnoreCase(java.lang.String,java.lang.String)}.
   */
  public ParkingSpaces findFirstByLocationIgnoreCaseAndCityIgnoreCase(String loc, String city) {
    String queryString = "SELECT p FROM ParkingSpaces p WHERE UPPER(p.location) = UPPER(:loc) AND UPPER(p.city) = UPPER(:city)";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("loc", loc != null ? loc.toUpperCase() : loc);
    query.setParameter("city", city != null ? city.toUpperCase() : city);
    if (query.getMaxResults() != Integer.MAX_VALUE) {
      if (query.getMaxResults() > 1 && query.getFirstResult() > 0) {
        query.setFirstResult(query.getFirstResult() - (query.getMaxResults() - 1));
      }
    }
    query.setMaxResults(1);

    return (ParkingSpaces) convertOne(query.getSingleResultOrNull(), false, ParkingSpaces.class);
  }

  /**
   * AOT generated implementation of {@link ParkingSpacesRepository#findLocationSuggestions(java.lang.String)}.
   */
  public List<String> findLocationSuggestions(@Param("query") String query) {
    String query_1String = "    SELECT DISTINCT CONCAT(p.location, ', ', p.city)\n"
            + "    FROM ParkingSpaces p\n"
            + "    WHERE LOWER(p.location) LIKE LOWER(CONCAT('%', :query, '%'))\n"
            + "       OR LOWER(p.city) LIKE LOWER(CONCAT('%', :query, '%'))\n";
    Query query_1 = this.entityManager.createQuery(query_1String);
    query_1.setParameter("query", query);

    return (List<String>) convertMany(query_1.getResultList(), false, String.class);
  }
}
