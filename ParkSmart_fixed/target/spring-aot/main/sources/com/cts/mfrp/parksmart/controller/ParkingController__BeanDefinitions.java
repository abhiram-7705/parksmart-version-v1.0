package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ParkingController}.
 */
@Generated
public class ParkingController__BeanDefinitions {
  /**
   * Get the bean definition for 'parkingController'.
   */
  public static BeanDefinition getParkingControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ParkingController.class);
    InstanceSupplier<ParkingController> instanceSupplier = InstanceSupplier.using(ParkingController::new);
    instanceSupplier = instanceSupplier.andThen(ParkingController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
