package com.xman.message.codec.support;

import com.alibaba.fastjson.JSON;
import com.xman.message.codec.Codec;

import java.nio.ByteBuffer;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 23:10
 */
public class FastJsonCodec implements Codec {
    @Override
    public void encode(ByteBuffer buffer, Object message) {
        byte[] data = JSON.toJSONBytes(message);
        buffer.wrap(data);
    }

    @Override
    public Object decode(ByteBuffer buffer) {

        return JSON.parse(buffer.array());
    }
}
