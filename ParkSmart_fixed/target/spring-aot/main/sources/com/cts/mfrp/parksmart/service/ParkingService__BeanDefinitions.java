package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ParkingService}.
 */
@Generated
public class ParkingService__BeanDefinitions {
  /**
   * Get the bean definition for 'parkingService'.
   */
  public static BeanDefinition getParkingServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ParkingService.class);
    InstanceSupplier<ParkingService> instanceSupplier = InstanceSupplier.using(ParkingService::new);
    instanceSupplier = instanceSupplier.andThen(ParkingService__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
