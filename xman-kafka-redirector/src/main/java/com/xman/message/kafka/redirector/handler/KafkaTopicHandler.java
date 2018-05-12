package com.xman.message.kafka.redirector.handler;

import com.alibaba.fastjson.JSON;
import com.xman.message.annotation.MQHanlder;
import com.xman.message.annotation.RegisterMQ;
import com.xman.message.kafka.redirector.common.bo.TestData;
import com.xman.message.kafka.redirector.common.bo.TestItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@MQHanlder
@Component
public class KafkaTopicHandler {

    @Autowired
    HttpServiceImpl httpService;

    @Value("${http.url.redirector}")
    private String redirectorUrl;

    /**
     * Message flow:
     *  1. raw message is consumed from KAFKA topic 'topic-test-plain'.
     *  2. function 'handlePlainData' will be called with param data, do actions balabala...
     *  3. Bean of 'TestItemData' is created and it's Json string will be produced to KAFKA topic 'topic-test-json'.
     *
     *  Data from KAFKA topic is handled and push to another topic with action at other endpoint.
     * @param data
     * @return
     */
    @RegisterMQ(topic = "topic-test-plain", codec = "RAW", to = "topic-test-json")
    public TestItemData handlePlainData(String data) {
        System.out.println(data);
        TestData testData = new TestData();
        testData.setCreateDate(new Date());
        testData.setData(data);
        testData.setId(System.currentTimeMillis());
        return new TestItemData(testData);
    }

    /**
     * Message flow:
     *  1. raw message is consumed from KAFKA topic 'topic-test-plain'.
     *  2. function 'handleJsonData' will be called with param data, do actions balabala...
     *  3. The data is sent to URL 'redirectorUrl' as HTTP protocol.
     *
     *  Data from KAFKA topic is redirect to HTTP endpoint.
     * @param data
     * @return
     */
    @RegisterMQ(topic = "topic-test-json", codec="JSON")
    public void handleJsonData(TestData data) {
        System.out.println("handleJsonData.data=" + data);
        httpService.httpPost(redirectorUrl, JSON.toJSONString(data));
    }
}
