package com.xman.message.codec;

import java.nio.ByteBuffer;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 17:06
 */
public interface Codec {

    void encode(ByteBuffer buffer, Object message);

    Object decode(ByteBuffer buffer);
}
