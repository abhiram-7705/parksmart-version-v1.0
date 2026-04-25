package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ContactService}.
 */
@Generated
public class ContactService__BeanDefinitions {
  /**
   * Get the bean definition for 'contactService'.
   */
  public static BeanDefinition getContactServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ContactService.class);
    InstanceSupplier<ContactService> instanceSupplier = InstanceSupplier.using(ContactService::new);
    instanceSupplier = instanceSupplier.andThen(ContactService__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
