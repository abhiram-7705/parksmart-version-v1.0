package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ParkingService}.
 */
@Generated
public class ParkingService__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ParkingService apply(RegisteredBean registeredBean, ParkingService instance) {
    AutowiredFieldValueResolver.forRequiredField("parkingSlotsRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("parkingSpacesRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("bookingsRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("slotHoldRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("userRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
