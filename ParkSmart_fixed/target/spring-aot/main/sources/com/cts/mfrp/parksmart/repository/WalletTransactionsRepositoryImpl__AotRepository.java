package com.cts.mfrp.parksmart.repository;

import com.cts.mfrp.parksmart.model.Users;
import com.cts.mfrp.parksmart.model.WalletTransactions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link WalletTransactionsRepository}.
 */
@Generated
public class WalletTransactionsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public WalletTransactionsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link WalletTransactionsRepository#findByUserOrderByTimestampDesc(com.cts.mfrp.parksmart.model.Users)}.
   */
  public List<WalletTransactions> findByUserOrderByTimestampDesc(Users user) {
    String queryString = "SELECT w FROM WalletTransactions w WHERE w.user = :user ORDER BY w.timestamp desc";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("user", user);

    return (List<WalletTransactions>) query.getResultList();
  }
}
