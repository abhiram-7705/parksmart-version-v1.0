package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link SearchService}.
 */
@Generated
public class SearchService__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static SearchService apply(RegisteredBean registeredBean, SearchService instance) {
    AutowiredFieldValueResolver.forRequiredField("parkingSpaceRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("parkingSlotsRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("bookingsRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
