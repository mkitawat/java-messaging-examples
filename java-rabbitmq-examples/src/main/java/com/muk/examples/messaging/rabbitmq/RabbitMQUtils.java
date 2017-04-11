package com.muk.examples.messaging.rabbitmq;

import com.rabbitmq.client.AMQP.BasicProperties;

public class RabbitMQUtils {

  public static String toString(BasicProperties properties) {
    StringBuilder builder = new StringBuilder();
    properties.appendPropertyDebugStringTo(builder);
    return builder.toString();
  }
  
  public static String user() {
    return System.getenv("RABBITMQ_USER");
  }
  
  public static String password() {
    return System.getenv("RABBITMQ_PASSWORD");
  }
  
  public static String uri() {
    return System.getenv("RABBITMQ_URI");
  }
}
