package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link SearchController}.
 */
@Generated
public class SearchController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static SearchController apply(RegisteredBean registeredBean, SearchController instance) {
    AutowiredFieldValueResolver.forRequiredField("searchService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
