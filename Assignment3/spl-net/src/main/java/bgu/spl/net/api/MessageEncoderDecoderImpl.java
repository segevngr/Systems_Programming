package bgu.spl.net.api;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder{


    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;


    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\u0000') {
            return (String) popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    //return byts
    public byte[] encode(String message) {
        return (message + "\u0000").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private java.lang.String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        java.lang.String result = new java.lang.String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

}
