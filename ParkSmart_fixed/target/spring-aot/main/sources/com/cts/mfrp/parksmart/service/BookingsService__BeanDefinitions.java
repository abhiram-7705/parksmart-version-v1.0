package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BookingsService}.
 */
@Generated
public class BookingsService__BeanDefinitions {
  /**
   * Get the bean definition for 'bookingsService'.
   */
  public static BeanDefinition getBookingsServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BookingsService.class);
    InstanceSupplier<BookingsService> instanceSupplier = InstanceSupplier.using(BookingsService::new);
    instanceSupplier = instanceSupplier.andThen(BookingsService__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
