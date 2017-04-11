package com.muk.examples.messaging.rabbitmq.batch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.muk.examples.messaging.rabbitmq.RabbitMQUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class BatchWorker {
  private static final Logger LOG = LoggerFactory.getLogger(BatchWorker.class);

  public static void main(String[] args) throws Exception {
    String queue = "hello";
    
    ConnectionFactory cf = new ConnectionFactory();
    cf.setUri(RabbitMQUtils.uri());
    cf.setUsername(RabbitMQUtils.user());
    cf.setPassword(RabbitMQUtils.password());
    Connection conn = cf.newConnection();
    /*Channel channel = conn.createChannel();
    channel.basicQos(1, true);*/
    ChannelConsumer consumer1 = new ChannelConsumer(conn, null, queue);
    ChannelConsumer consumer2 = new ChannelConsumer(conn, null, queue);
    
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        LOG.info("####### CLOSING CONN #########");
        try {
          conn.close();
        } catch (Exception e) {
          LOG.info("####### ERROR CLOSING CONN #########", e);
        }
        LOG.info("####### CLOSED CONN #########");
      }
    });

  }
  
  public static class ChannelConsumer extends DefaultConsumer {
    private static int SEQUENCE = 1;
    int id;
    String queue = null;
    boolean autoAck = false;
    String consumerTag;
    
    public ChannelConsumer(Connection conn, Channel channel, String queue) throws IOException {
      super(channel != null ? channel : conn.createChannel());
      this.id = SEQUENCE++;
      this.queue = queue;
      getChannel().basicQos(1, true);
      Map<String, Object> queueArgs = new HashMap<String, Object>();
      queueArgs.put("x-max-priority", 2);
      this.getChannel().queueDeclare(queue, true, false, false, queueArgs);
      
      consumerTag = this.getChannel().basicConsume(queue, autoAck, this);
    }
    
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
        byte[] body) throws IOException {
      LOG.info("id={}, body={}, props={}, env={}, ct1={}, ct2={}", 
          id, new String(body), RabbitMQUtils.toString(properties), 
          envelope, this.consumerTag, consumerTag);
      //this.getChannel().basicAck(envelope.getDeliveryTag(), false);
      if (envelope.isRedeliver()) {
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
      } else {
        this.getChannel().basicNack(envelope.getDeliveryTag(), false, true);
      }
    }
  }

}
