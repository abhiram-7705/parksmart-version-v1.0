package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ParkingController}.
 */
@Generated
public class ParkingController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ParkingController apply(RegisteredBean registeredBean, ParkingController instance) {
    AutowiredFieldValueResolver.forRequiredField("parkingService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
