package com.cts.mfrp.parksmart;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ParkSmartApplication}.
 */
@Generated
public class ParkSmartApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'parkSmartApplication'.
   */
  public static BeanDefinition getParkSmartApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ParkSmartApplication.class);
    beanDefinition.setInstanceSupplier(ParkSmartApplication::new);
    return beanDefinition;
  }
}
