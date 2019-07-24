package com.fh.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

public class MyMessageConverter {

    // json格式发送消息
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
