package com.cts.mfrp.parksmart.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link EmailService}.
 */
@Generated
public class EmailService__BeanDefinitions {
  /**
   * Get the bean definition for 'emailService'.
   */
  public static BeanDefinition getEmailServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(EmailService.class);
    beanDefinition.setInstanceSupplier(EmailService::new);
    return beanDefinition;
  }
}
