package com.cts.mfrp.parksmart.repository;

import com.cts.mfrp.parksmart.model.SlotHold;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link SlotHoldRepository}.
 */
@Generated
public class SlotHoldRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public SlotHoldRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link SlotHoldRepository#deleteByExpiresAtBefore(java.time.LocalDateTime)}.
   */
  public void deleteByExpiresAtBefore(LocalDateTime time) {
    String queryString = "SELECT s FROM SlotHold s WHERE s.expiresAt < :time";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("time", time);

    List resultList = query.getResultList();
    resultList.forEach(entityManager::remove);
    return;
  }

  /**
   * AOT generated implementation of {@link SlotHoldRepository#deleteByHoldGroupIdAndUserEmail(java.lang.String,java.lang.String)}.
   */
  public void deleteByHoldGroupIdAndUserEmail(String holdGroupId, String email) {
    String queryString = "SELECT s FROM SlotHold s LEFT JOIN s.user u WHERE s.holdGroupId = :holdGroupId AND u.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("holdGroupId", holdGroupId);
    query.setParameter("email", email);

    List resultList = query.getResultList();
    resultList.forEach(entityManager::remove);
    return;
  }

  /**
   * AOT generated implementation of {@link SlotHoldRepository#existsBySlotSlotIdAndArrivalLessThanAndLeavingGreaterThanAndExpiresAtAfter(java.lang.Integer,java.time.LocalDateTime,java.time.LocalDateTime,java.time.LocalDateTime)}.
   */
  public boolean existsBySlotSlotIdAndArrivalLessThanAndLeavingGreaterThanAndExpiresAtAfter(
      Integer slotId, LocalDateTime arrival, LocalDateTime leaving, LocalDateTime now) {
    String queryString = "SELECT s.holdId FROM SlotHold s WHERE s.slot.slotId = :slotId AND s.arrival < :arrival AND s.leaving > :leaving AND s.expiresAt > :now";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("slotId", slotId);
    query.setParameter("arrival", arrival);
    query.setParameter("leaving", leaving);
    query.setParameter("now", now);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link SlotHoldRepository#findByHoldGroupId(java.lang.String)}.
   */
  public List<SlotHold> findByHoldGroupId(String holdGroupId) {
    String queryString = "SELECT s FROM SlotHold s WHERE s.holdGroupId = :holdGroupId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("holdGroupId", holdGroupId);

    return (List<SlotHold>) query.getResultList();
  }
}
