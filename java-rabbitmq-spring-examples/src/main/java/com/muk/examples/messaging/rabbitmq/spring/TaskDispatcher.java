package com.muk.examples.messaging.rabbitmq.spring;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class TaskDispatcher implements ChannelAwareMessageListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskDispatcher.class);
  
  @Override
  public void onMessage(Message message, Channel channel) throws Exception {
    LOGGER.info("Received Message: " + message.toString());
    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
  }

}
