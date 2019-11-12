package com.jdr.maven.rabbitmq.delay.delaymessage;

import com.jdr.maven.rabbitmq.delay.delaymessage.service.delay.IDelayMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DelayMessageApplicationTests {

    @Resource(name = "rabbitMqDelayMessage")
    private IDelayMessage delayMessage;

    @Test
    public void testRabbitMqDelayMessage() {
        delayMessage.productMessage();
    }

}
