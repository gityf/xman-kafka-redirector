package com.xman.message.codec;

/**
 * Created by wangyofu on 2015/9/18.
 */
public enum CodecCode {
    CODEC_JSON(1, "JSON"),
    CODEC_RAW(2, "RAW"),
    CODEC_XML(2, "RAW"),
    ;

    private Integer code;
    private String comment;

    CodecCode(Integer code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public Integer getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

    public static CodecCode getByCode(int code) {
        switch (code) {
            case 1:
                return CODEC_JSON;
            case 2:
                return CODEC_RAW;
            case 3:
                return CODEC_XML;
            default:
                return CODEC_JSON;
        }
    }
}
