package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link DashboardController}.
 */
@Generated
public class DashboardController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static DashboardController apply(RegisteredBean registeredBean,
      DashboardController instance) {
    AutowiredFieldValueResolver.forRequiredField("dashboardService").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("contactService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
