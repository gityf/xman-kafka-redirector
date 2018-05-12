package com.xman.message.codec;


import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 17:12
 */
public class CodecAdapter implements Codec {
    private Codec codec;

    public CodecAdapter(Codec codec) {
        this.codec = codec;
    }


    @Override
    public void encode(ByteBuffer buffer, Object message) {
        // TODO 序列化
    }

    @Override
    public Map<String, Object> decode(ByteBuffer buffer) {
        // TODO 反序列化
        return null;
    }
}
