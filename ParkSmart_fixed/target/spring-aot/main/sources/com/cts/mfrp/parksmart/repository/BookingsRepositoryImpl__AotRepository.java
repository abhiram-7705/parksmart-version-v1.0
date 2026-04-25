package com.cts.mfrp.parksmart.repository;

import com.cts.mfrp.parksmart.model.Bookings;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.Param;

/**
 * AOT generated JPA repository implementation for {@link BookingsRepository}.
 */
@Generated
public class BookingsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public BookingsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link BookingsRepository#existsActiveOrUpcomingBySlotId(java.lang.Integer)}.
   */
  public boolean existsActiveOrUpcomingBySlotId(@Param("slotId") Integer slotId) {
    String queryString = "SELECT COUNT(b) > 0 FROM Bookings b\n"
            + "WHERE b.parkingSlot.slotId = :slotId\n"
            + "AND b.status IN ('CONFIRMED','UPCOMING','ACTIVE')\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("slotId", slotId);

    return (Boolean) convertOne(query.getSingleResultOrNull(), false, Boolean.class);
  }

  /**
   * AOT generated implementation of {@link BookingsRepository#existsActiveOrUpcomingBySpaceId(java.lang.Integer)}.
   */
  public boolean existsActiveOrUpcomingBySpaceId(@Param("spaceId") Integer spaceId) {
    String queryString = "SELECT COUNT(b) > 0 FROM Bookings b\n"
            + "JOIN b.parkingSlot s\n"
            + "WHERE s.parkingSpace.spaceId = :spaceId\n"
            + "AND b.status IN ('CONFIRMED','UPCOMING','ACTIVE')\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("spaceId", spaceId);

    return (Boolean) convertOne(query.getSingleResultOrNull(), false, Boolean.class);
  }

  /**
   * AOT generated implementation of {@link BookingsRepository#existsByParkingSlotSlotIdAndArrivalLessThanAndLeavingGreaterThan(java.lang.Integer,java.time.LocalDateTime,java.time.LocalDateTime)}.
   */
  public boolean existsByParkingSlotSlotIdAndArrivalLessThanAndLeavingGreaterThan(Integer slotId,
      LocalDateTime arrival, LocalDateTime leaving) {
    String queryString = "SELECT b.bookingId FROM Bookings b WHERE b.parkingSlot.slotId = :slotId AND b.arrival < :arrival AND b.leaving > :leaving";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("slotId", slotId);
    query.setParameter("arrival", arrival);
    query.setParameter("leaving", leaving);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link BookingsRepository#findBySpaceId(java.lang.Integer)}.
   */
  public List<Bookings> findBySpaceId(@Param("spaceId") Integer spaceId) {
    String queryString = "SELECT b FROM Bookings b\n"
            + "JOIN b.parkingSlot s\n"
            + "WHERE s.parkingSpace.spaceId = :spaceId\n"
            + "ORDER BY b.arrival DESC\n";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("spaceId", spaceId);

    return (List<Bookings>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link BookingsRepository#findByUserId(int)}.
   */
  public List<Bookings> findByUserId(@Param("userId") int userId) {
    String queryString = "SELECT b FROM Bookings b WHERE b.user.userId = :userId ORDER BY b.arrival DESC";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("userId", userId);

    return (List<Bookings>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link BookingsRepository#findSuggestionsByUserAndQuery(java.lang.String,java.lang.String)}.
   */
  public List<String> findSuggestionsByUserAndQuery(@Param("email") String email,
      @Param("query") String query) {
    String query_1String = "SELECT ps.name\n"
            + "FROM Bookings b\n"
            + "JOIN b.parkingSlot s\n"
            + "JOIN s.parkingSpace ps\n"
            + "WHERE b.user.email = :email\n"
            + "AND (\n"
            + "    LOWER(ps.name) LIKE LOWER(CONCAT('%', :query, '%')) OR\n"
            + "    LOWER(ps.location) LIKE LOWER(CONCAT('%', :query, '%')) OR\n"
            + "    LOWER(ps.city) LIKE LOWER(CONCAT('%', :query, '%'))\n"
            + ")\n";
    Query query_1 = this.entityManager.createQuery(query_1String);
    query_1.setParameter("email", email);
    query_1.setParameter("query", query);

    return (List<String>) convertMany(query_1.getResultList(), false, String.class);
  }
}
