package com.fh.controller;

import com.fh.domain.Student;
import com.google.gson.Gson;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MessageSendController {

    @Autowired
    @Qualifier("fanoutTemplate")
    RabbitTemplate fanoutTemplate;

    @Autowired
    @Qualifier("directTemplate")
    RabbitTemplate directTemplate;

    @Autowired
    @Qualifier("topicTemplate")
    RabbitTemplate topicTemplate;

    @Autowired
    @Qualifier("myTemplate")
    RabbitTemplate myTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @RequestMapping(value = "sendToAll",method = RequestMethod.GET)
    public String sendToAll () throws Exception{
        Gson gson = new Gson();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//24小时制
        //发送消息
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("type", "1");
        msg.put("date", date);
        fanoutTemplate.convertAndSend("directQueue", gson.toJson(msg));
        Thread.sleep(1000);// 休眠1秒
        return "index";
    }

    @RequestMapping(value = "sendToDirect",method = RequestMethod.GET)
    public String sendToDirect () throws Exception{
        Gson gson = new Gson();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//24小时制
        //发送消息
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("type", "1");
        msg.put("date", date);
        directTemplate.convertAndSend("routekey1", gson.toJson(msg));
        Thread.sleep(1000);// 休眠1秒
        return "index";
    }

    @RequestMapping(value = "sendToTopic",method = RequestMethod.GET)
    public String sendToTopic () throws Exception{
        Gson gson = new Gson();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//24小时制
        //发送消息
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("type", "1");
        msg.put("date", date);
        topicTemplate.convertAndSend("ccc.new", gson.toJson(msg));
        Thread.sleep(1000);// 休眠1秒
        return "index";
    }

    // 创建交换机和消息队列
    @RequestMapping(value = "createFanoutExchange")
    public String createFanoutExchange(){
        // 创建交换机 DirectExchange表示点对点交换机 如果想设置广播或选择，可以选择对应的java类
        amqpAdmin.declareExchange(new DirectExchange("amqpAdmin:direct"));
        amqpAdmin.declareQueue(new Queue("amqpAdmin.queue1"));
        amqpAdmin.declareQueue(new Queue("amqpAdmin.queue2"));
        // 绑定
        //String destination 目的地，即输出到哪
        // Binding.DestinationType destinationType 绑定的类型 这是个枚举类型:QUEUE和EXCHANGE
        // String exchange 交换机的名字
        // String routingKey 路由键
        // Map<String, Object> arguments 一些消息参数，如果没有则为null
        amqpAdmin.declareBinding(
                new Binding("amqpAdmin.queue1", Binding.DestinationType.QUEUE,
                        "amqpAdmin:direct","queue01",null));
        amqpAdmin.declareBinding(
                new Binding("amqpAdmin.queue2", Binding.DestinationType.QUEUE,
                        "amqpAdmin:direct","queue02",null));
        return "index";
    }


    //  动态指定发送给哪一个交换机
    @RequestMapping(value = "sendToCustomDirect",method = RequestMethod.GET)
    public String sendToCustomDirect () throws Exception{
        myTemplate.convertAndSend("amqpAdmin:direct", "queue02",
                new Student("吴兴玉","20","江苏连云港"));
        Thread.sleep(1000);// 休眠1秒
        return "index";
    }

}
