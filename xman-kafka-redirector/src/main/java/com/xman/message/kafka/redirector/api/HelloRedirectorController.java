package com.xman.message.kafka.redirector.api;

import com.xman.message.kafka.redirector.common.bo.TestData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangyaofu on 2018/5/5.
 */
@RestController
public class HelloRedirectorController {

    @RequestMapping("/kafka/topic-test-json/data")
    @ResponseBody
    public Object kafkaMessage(@RequestBody TestData data) {
        System.out.println("redirector.kafka.message.data=" + data);
        return data;
    }
}
