package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ContactService}.
 */
@Generated
public class ContactService__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ContactService apply(RegisteredBean registeredBean, ContactService instance) {
    AutowiredFieldValueResolver.forRequiredField("contactRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("userRepository").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("parkingSpacesRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
