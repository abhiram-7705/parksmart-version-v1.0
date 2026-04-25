package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link OwnerDashboardController}.
 */
@Generated
public class OwnerDashboardController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static OwnerDashboardController apply(RegisteredBean registeredBean,
      OwnerDashboardController instance) {
    AutowiredFieldValueResolver.forRequiredField("ownerDashboardService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
