package com.muk.examples.messaging.rabbitmq.batch;

import java.util.HashMap;
import java.util.Map;

import com.muk.examples.messaging.rabbitmq.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class BatchRequester {

  public static void main(String[] args) throws Exception {
    String queue = "hello";
    String message = args[0];
    
    ConnectionFactory cf = new ConnectionFactory();
    cf.setUri(RabbitMQUtils.uri());
    cf.setUsername(RabbitMQUtils.user());
    cf.setPassword(RabbitMQUtils.password());
    Connection conn = cf.newConnection();
    
    Channel channel = conn.createChannel();
    
    Map<String, Object> queueArgs = new HashMap<String, Object>();
    queueArgs.put("x-max-priority", 2);
    
    channel.queueDeclare(queue, true, false, false, queueArgs);
    AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
        .deliveryMode(2)
        .priority(1)
        .build();
    //channel.basicPublish("", queue, MessageProperties.MINIMAL_PERSISTENT_BASIC, message.getBytes());
    channel.basicPublish("", queue, props, message.getBytes());
    
    channel.close();
    
    conn.close();
  }

}
