package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link BookingsService}.
 */
@Generated
public class BookingsService__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static BookingsService apply(RegisteredBean registeredBean, BookingsService instance) {
    AutowiredFieldValueResolver.forRequiredField("userRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("slotHoldRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("parkingSpacesRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("promoCodeRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("walletTransactionsRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("bookingsRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
