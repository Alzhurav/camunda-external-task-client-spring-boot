package org.camunda.bpm.client.spring.boot.starter;

import org.camunda.bpm.client.spring.EnableTaskSubscription;
import org.camunda.bpm.client.spring.TaskSubscriptionConfiguration.ClientConfig;
import org.camunda.bpm.client.spring.boot.starter.CamundaBpmClientAutoConfiguration.PropertiesAwareClientRegistrar;
import org.camunda.bpm.client.spring.boot.starter.task.PropertiesAwareExternalTaskClientFactory;
import org.camunda.bpm.client.spring.boot.starter.task.PropertiesAwareSubscribedExternalTaskBean;
import org.camunda.bpm.client.spring.context.ClientRegistrar;
import org.camunda.bpm.client.spring.context.ExternalTaskBeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;

@EnableConfigurationProperties({ CamundaBpmClientProperties.class })
@EnableTaskSubscription
@Import({ ClientConfig.class, PropertiesAwareClientRegistrar.class })
@Configuration
public class CamundaBpmClientAutoConfiguration {

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public static BeanDefinitionRegistryPostProcessor externalTaskBeanDefinitionRegistryPostProcessor() {
    return new ExternalTaskBeanDefinitionRegistryPostProcessor(PropertiesAwareSubscribedExternalTaskBean.class);
  }

  static class PropertiesAwareClientRegistrar extends ClientRegistrar {
    public PropertiesAwareClientRegistrar() {
      super(PropertiesAwareExternalTaskClientFactory.class);
    }

    @Override
    protected BeanDefinitionBuilder getExternalTaskClientFactoryBeanDefinitionBuilder(
        AnnotationAttributes enableTaskSubscription, String id) {
      return super.getExternalTaskClientFactoryBeanDefinitionBuilder(enableTaskSubscription, id).addPropertyValue("id",
          id);
    }
  }
}
