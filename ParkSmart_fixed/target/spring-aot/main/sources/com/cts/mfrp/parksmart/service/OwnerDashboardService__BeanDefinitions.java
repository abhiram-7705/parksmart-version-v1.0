package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link OwnerDashboardService}.
 */
@Generated
public class OwnerDashboardService__BeanDefinitions {
  /**
   * Get the bean definition for 'ownerDashboardService'.
   */
  public static BeanDefinition getOwnerDashboardServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(OwnerDashboardService.class);
    InstanceSupplier<OwnerDashboardService> instanceSupplier = InstanceSupplier.using(OwnerDashboardService::new);
    instanceSupplier = instanceSupplier.andThen(OwnerDashboardService__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
