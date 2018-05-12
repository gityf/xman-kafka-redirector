package com.xman.message.kafka.redirector.common.bo;

/**
 * Created by wangyaofu on 2018/5/5.
 */
public class TestItemData {
    private TestData data;

    public TestItemData(TestData data) {
        this.data = data;
    }

    public TestData getData() {
        return data;
    }

    public void setData(TestData data) {
        this.data = data;
    }
}
