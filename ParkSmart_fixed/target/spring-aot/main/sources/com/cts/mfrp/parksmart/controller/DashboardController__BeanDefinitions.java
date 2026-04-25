package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DashboardController}.
 */
@Generated
public class DashboardController__BeanDefinitions {
  /**
   * Get the bean definition for 'dashboardController'.
   */
  public static BeanDefinition getDashboardControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DashboardController.class);
    InstanceSupplier<DashboardController> instanceSupplier = InstanceSupplier.using(DashboardController::new);
    instanceSupplier = instanceSupplier.andThen(DashboardController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
