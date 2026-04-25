package com.cts.mfrp.parksmart.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SearchController}.
 */
@Generated
public class SearchController__BeanDefinitions {
  /**
   * Get the bean definition for 'searchController'.
   */
  public static BeanDefinition getSearchControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SearchController.class);
    InstanceSupplier<SearchController> instanceSupplier = InstanceSupplier.using(SearchController::new);
    instanceSupplier = instanceSupplier.andThen(SearchController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
