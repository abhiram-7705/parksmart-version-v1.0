package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BookingsController}.
 */
@Generated
public class BookingsController__BeanDefinitions {
  /**
   * Get the bean definition for 'bookingsController'.
   */
  public static BeanDefinition getBookingsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BookingsController.class);
    InstanceSupplier<BookingsController> instanceSupplier = InstanceSupplier.using(BookingsController::new);
    instanceSupplier = instanceSupplier.andThen(BookingsController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
