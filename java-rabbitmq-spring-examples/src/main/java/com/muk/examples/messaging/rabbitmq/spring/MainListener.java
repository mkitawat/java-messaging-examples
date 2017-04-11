package com.muk.examples.messaging.rabbitmq.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainListener.class);
  
  public static void main(String[] args) {
    LOGGER.info("Starting listener");
    @SuppressWarnings("resource")
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("listener-context.xml");
    context.registerShutdownHook();
    LOGGER.info("Started listener");
  }

}
