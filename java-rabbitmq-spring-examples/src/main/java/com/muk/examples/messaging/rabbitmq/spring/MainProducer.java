package com.muk.examples.messaging.rabbitmq.spring;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainProducer {

  public static void main(String[] args) {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("producer-context.xml");
    context.registerShutdownHook();
    Queue taskQueue = context.getBean("taskQueue", Queue.class);
    RabbitAdmin rabbitAdmin = context.getBean("rabbitAdmin", RabbitAdmin.class);
    rabbitAdmin.declareQueue(taskQueue);
    RabbitTemplate rabbitTemplate = context.getBean("rabbitTemplate", RabbitTemplate.class);
    Message message = MessageBuilder.withBody(args[0]
        .getBytes())
        .setContentType("text/plain")
        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
        .build();
    rabbitTemplate.send(message);
    context.close();
  }

}
