# xman-mq-redirector
redirector of KAFKA or RocketMQ.
the message will be consumed from MQ then sent to endpoint by HTTP protocol.

# annotation description
## MQHandler
    This is mq-handler annotation add for class TopicHandler.
    
## RegisterMQ
    This is topic register which bind to MQ(KAFKA or RocketMQ).
    topic
        which kafka topic U want to subscribe and consume.
    to
        topic of next hup, the data will be handled and push to another topic as data-source for next endpoint.
    codec
        codec of data, JSON and RAW is supported now.
    keys
        which keys will be filter to check data is what U need.
    values
        what value of each key.
    cr
        and or or action of key-value pairs.
        such as:
        if key = name, age
           values = name-i-need, 18
           cr = AND
        
        then 
           the message with key name equal to 'name-i-need' adn age is 18 will be handled by TpoicHandler.
    'keys + values + cr' is like to tag in RocketMQ.

# kafka-redirector
`code java
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
`