package com.xman.message.codec;

import com.xman.message.codec.support.FastJsonCodec;
import com.xman.message.AbstractTest;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author yangxiang@
 * @Date 2015/11/30
 * @Time 12:24
 */
public class FastJsonCodecTest extends AbstractTest {

    private String data = "{\"data\":\"tx\"}";

    FastJsonCodec codec = new FastJsonCodec();

    @Test
    public void testEncode() {
        String message = "Hello world";
        ByteBuffer buffer = ByteBuffer.allocate(message.getBytes().length);
        codec.encode(buffer, message);
        assertTrue(buffer.remaining() > 0);
    }

    @Test
    public void testDecode() {
//        Request a = new Request();
//        a.setData("xxx");
//        ByteBuffer buffer = ByteBuffer.allocate(45);
//        codec.encode(buffer, a);
        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
        Object result = codec.decode(buffer);
        assertNotNull(result);
    }

    class Request {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
