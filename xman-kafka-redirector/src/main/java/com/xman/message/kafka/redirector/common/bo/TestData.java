package com.xman.message.kafka.redirector.common.bo;

import java.util.Date;

/**
 * Created by wangyaofu on 2018/5/5.
 */
public class TestData {
    private Long id;
    private Date createDate;
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", data='" + data + '\'' +
                '}';
    }
}
