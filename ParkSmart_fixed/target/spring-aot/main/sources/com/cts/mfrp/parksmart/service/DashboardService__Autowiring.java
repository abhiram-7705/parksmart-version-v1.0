package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link DashboardService}.
 */
@Generated
public class DashboardService__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static DashboardService apply(RegisteredBean registeredBean, DashboardService instance) {
    AutowiredFieldValueResolver.forRequiredField("bookingsRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("userRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("walletTransactionsRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("reviewsRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
