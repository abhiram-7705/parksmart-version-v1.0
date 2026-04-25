package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link OwnerDashboardController}.
 */
@Generated
public class OwnerDashboardController__BeanDefinitions {
  /**
   * Get the bean definition for 'ownerDashboardController'.
   */
  public static BeanDefinition getOwnerDashboardControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(OwnerDashboardController.class);
    InstanceSupplier<OwnerDashboardController> instanceSupplier = InstanceSupplier.using(OwnerDashboardController::new);
    instanceSupplier = instanceSupplier.andThen(OwnerDashboardController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
