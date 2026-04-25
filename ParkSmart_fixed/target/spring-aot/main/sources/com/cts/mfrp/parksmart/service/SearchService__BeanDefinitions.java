package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SearchService}.
 */
@Generated
public class SearchService__BeanDefinitions {
  /**
   * Get the bean definition for 'searchService'.
   */
  public static BeanDefinition getSearchServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SearchService.class);
    InstanceSupplier<SearchService> instanceSupplier = InstanceSupplier.using(SearchService::new);
    instanceSupplier = instanceSupplier.andThen(SearchService__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
