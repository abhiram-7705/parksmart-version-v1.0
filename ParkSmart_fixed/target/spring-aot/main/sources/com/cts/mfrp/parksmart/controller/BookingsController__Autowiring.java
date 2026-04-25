package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link BookingsController}.
 */
@Generated
public class BookingsController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static BookingsController apply(RegisteredBean registeredBean,
      BookingsController instance) {
    AutowiredFieldValueResolver.forRequiredField("bookingsService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
